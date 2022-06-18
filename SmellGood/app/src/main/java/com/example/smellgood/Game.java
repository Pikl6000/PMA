package com.example.smellgood;
import android.content.AsyncQueryHandler;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class Game extends AppCompatActivity {
    FirebaseAuth mAuth;
    private FirebaseUser user;
    private Handler handler;
    private Timer timer;
    private ImageView robo, mud, powder, bottom, totem;
    private Button playButton;
    private TextView scoreText, totemText;
    private LinearLayout gamePanel;
    private int period, totemCount, scoreCount, media_length;
    private float roboX, mudY, powderY,totemY, width, height;
    private boolean right = false, isMoving = false, firstGen = true, mudHit = false, powderHit = false, totemHit = false;
    private boolean firstChange = true, roboRight;
    private int[] listOfImages;
    private long startTime;
    private static final int INSERT_NOTE_TOKEN = 0;
    private Fdata data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        setContentView(R.layout.game_layout);
        checkInternet();
        data = Main.data;

        mAuth = FirebaseAuth.getInstance();

        user = mAuth.getCurrentUser();
        if (user == null){
            startActivity(new Intent(Game.this, LoginActivity.class));
        }

        period = 1;
        scoreCount = 0;
        totemCount = 0;
        media_length = 0;

        robo = findViewById(R.id.robo);
        totemText = findViewById(R.id.totem);
        scoreText = findViewById(R.id.score);
        gamePanel = findViewById(R.id.gamePanel);
        playButton = findViewById(R.id.startButton);
        mud = findViewById(R.id.mud);
        powder = findViewById(R.id.powder);
        bottom = findViewById(R.id.bottom);
        totem = findViewById(R.id.totemObject);
        right = true;
        handler = new Handler(Looper.getMainLooper());
        updateRobo();
    }

    public void checkInternet(){
        if (!isNetworkAvailable()){
            startActivity(new Intent(Game.this, NoInternet.class));
        }
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void updateRobo(){
        int id = Main.roboid;
        if (id == 1){
            robo.setImageResource(R.drawable.robostand);
            listOfImages = new int[]{R.drawable.robostand, R.drawable.robostandl, R.drawable.robodeadright, R.drawable.robodeadleft};
        }
        if (id == 2){
            robo.setImageResource(R.drawable.robostandpink);
            listOfImages = new int[]{R.drawable.robostandpink, R.drawable.robostandpinkl, R.drawable.robodeadrightpink, R.drawable.robodeadleftpink};
        }
        if (id == 3){
            robo.setImageResource(R.drawable.robostandblue);
            listOfImages = new int[]{R.drawable.robostandblue, R.drawable.robostandbluel, R.drawable.robodeadrightblue, R.drawable.robodeadleftblue};
        }
        if (id == 4){
            robo.setImageResource(R.drawable.robostandwhite);
            listOfImages = new int[]{R.drawable.robostandwhite, R.drawable.robostandwhitel, R.drawable.robodeadrightwhite, R.drawable.robodeadlefttwhite};
        }
    }

    public void startGame(View view) {
        width = gamePanel.getWidth();
        height = gamePanel.getHeight() - bottom.getHeight() - mud.getHeight();
        mudHit = false;
        if (totemCount > 0){
            totemCount--;
        } else {
            scoreCount = 0;
            totemCount = 0;
        }
        scoreText.setText("Score : " + scoreCount);
        totemText.setText("Totem : " + totemCount);
        playButton.setVisibility(View.GONE);
        robo.setImageResource(listOfImages[0]);
        robo.setLayoutParams(new LinearLayout.LayoutParams(dpToPx(85), dpToPx(140)));
        generateMud();
        generatePowder();
        generateTotem();
        setTimer();
    }


    public void updateText() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                scoreText.setText("Score : " + scoreCount);
                totemText.setText("Totem : " + totemCount);
            }
        });
    }

    public void setTimer() {
        if (isMoving) {
            return;
        } else {
            isMoving = true;
            timer = new Timer();
            startTime = System.currentTimeMillis();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    moveRobo();
                    if (mud.getVisibility() != View.GONE) fallAnimation(true, false);
                    if (powder.getVisibility() != View.GONE) fallAnimation(false, true);
                    if (totem.getVisibility() != View.GONE) fallAnimation(false, false);
                    updateText();
                }
            }, 0, period);
        }
    }


    public void moveRobo() {
        //pohyb hracej postavy
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isMoving) {
                    if (right) {
                        if (firstChange) {
                            firstChange = false;
                            robo.setImageResource(listOfImages[0]);
                            roboRight = true;
                        }
                        if (roboX + robo.getWidth() >= gamePanel.getWidth()) {
                            roboX = gamePanel.getWidth() - robo.getWidth();
                            robo.setX(roboX);
                            firstChange = true;
                        } else {
                            roboX += 1;
                            robo.setX(roboX);
                        }
                    } else {
                        if (firstChange) {
                            firstChange = false;
                            robo.setImageResource(listOfImages[1]);
                            roboRight = false;
                        }
                        if ((robo.getX() - 1) < 0) {
                            roboX = 0;
                            robo.setX(roboX);
                            firstChange = true;
                        } else {
                            roboX -= 1;
                            robo.setX(roboX);
                        }
                    }
                }
            }
        });
    }

    public void fallAnimation(boolean isMud, boolean isPowder){
        if (isMoving) {
            if (isMud){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mudY += 1.35;
                        mud.setY(mudY);
                        if (collisionMud()) collidedMud();
                        if (mud.getY() >= height) generateMud();
                    }
                });
            } else if (isPowder){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        powderY += 1.35;
                        powder.setY(powderY);
                        if (collisionPowder()) collidedPowder();
                        if (powder.getY() >= height) generatePowder();
                    }
                });
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        totemY += 1.35;
                        totem.setY(totemY);
                        if (collisionTotem()) collidedTotem();
                        if (totem.getY() >= height) generateTotem();
                    }
                });
            }
        }

    }

    public void klik(View view) {
        firstChange = true;
        if (right) {
            right = false;
        } else right = true;
    }

    /* generovanie objektov, ktorym sa treba vyhybat */
    public void generateMud() {
        mud.setY(-100);
        float x;
        float [] img1Range = new float[2];
        float [] img2Range = new float[2];
        img1Range[0] = powder.getX() - powder.getWidth();
        img1Range[1] = powder.getX() + powder.getWidth();
        img2Range[0] = totem.getX() - totem.getWidth();
        img2Range[1] = totem.getX() + totem.getWidth();

        do {
            x = (float) (Math.random() * (gamePanel.getWidth() - mud.getWidth()));
        } while ((x >= img1Range[0] && x <= img1Range[1]) || (x >= img2Range[0] && x <= img2Range[1]));
        mud.setX(x);
        mudY = mud.getY();
        mud.setVisibility(View.VISIBLE);
    }

    /* generovanie objektov, ktore treba zbierat */
    public void generatePowder() {
        if (firstGen) {
            powder.setY(-500);
            firstGen = !firstGen;
        } else {
            powder.setY(-100);
        }
        float x;
        float [] img1Range = new float[2];
        float [] img2Range = new float[2];
        img1Range[0] = mud.getX() - mud.getWidth();
        img1Range[1] = mud.getX() + mud.getWidth();
        img2Range[0] = totem.getX() - totem.getWidth();
        img2Range[1] = totem.getX() + totem.getWidth();

        do {
            x = (float) (Math.random() * (gamePanel.getWidth() - powder.getWidth()));
        } while ((x >= img1Range[0] && x <= img1Range[1]) || (x >= img2Range[0] && x <= img2Range[1]));
        powder.setX(x);
        powderY = powder.getY();
        powder.setVisibility(View.VISIBLE);
    }

    public void generateTotem() {
        float x;
        float [] img1Range = new float[2];
        float [] img2Range = new float[2];
        img1Range[0] = mud.getX() - mud.getWidth();
        img1Range[1] = mud.getX() + mud.getWidth();
        img2Range[0] = powder.getX() - powder.getWidth();
        img2Range[1] = powder.getX() + powder.getWidth();

        do {
            x = (float) (Math.random() * (gamePanel.getWidth() - totem.getWidth()));
        } while ((x >= img1Range[0] && x <= img1Range[1]) || (x >= img2Range[0] && x <= img2Range[1]));
        totem.setY(-30000);
        totem.setX(x);
        totem.setVisibility(View.VISIBLE);
        totemY = totem.getY();
    }

    public boolean collisionMud() {
        float startX = mud.getX();
        float endX = startX + mud.getWidth();
        float endY = mud.getY() + mud.getHeight();
        float startRoboX = robo.getX();
        float endRoboX = startRoboX + robo.getWidth();
        float startRoboY = robo.getY();

        if (((startX >= startRoboX && startX <= endRoboX) && (endY >= startRoboY && endY <= startRoboY + robo.getHeight())) ||
                ((endX >= startRoboX && endX <= endRoboX) && (endY >= startRoboY && endY <= startRoboY + robo.getHeight()))
        ) return true;
        return false;
    }
    public void collidedMud(){
        checkInternet();
        stop();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (roboRight) {
                    robo.setImageResource(listOfImages[2]);
                } else{
                    robo.setImageResource(listOfImages[3]);
                }
                if (robo.getX() + robo.getWidth() >= width) {
                    roboX = width - robo.getWidth();
                    robo.setX(roboX);
                }
                robo.setLayoutParams(new LinearLayout.LayoutParams(dpToPx(140), dpToPx(85)));
                powder.setVisibility(View.GONE);
                mud.setVisibility(View.GONE);
                totem.setVisibility(View.GONE);
            }
        });
    }

    public boolean collisionPowder() {
        float startX = powder.getX();
        float endX = startX + powder.getWidth();
        float endY = powder.getY() + powder.getHeight();
        float startRoboX = robo.getX();
        float endRoboX = startRoboX + robo.getWidth();
        float startRoboY = robo.getY();

        if (((startX >= startRoboX && startX <= endRoboX) && (endY >= startRoboY && endY <= startRoboY + robo.getHeight())) ||
                ((endX >= startRoboX && endX <= endRoboX) && (endY >= startRoboY && endY <= startRoboY + robo.getHeight()))
        ) return true;
        return false;
    }

    public void collidedPowder(){
        generatePowder();
        scoreCount++;
    }

    public boolean collisionTotem() {
        float startX = totem.getX();
        float endX = startX + totem.getWidth();
        float endY = totem.getY() - totem.getHeight();
        float startRoboX = robo.getX();
        float endRoboX = startRoboX + robo.getWidth();
        float startRoboY = robo.getY();

        if (((startX >= startRoboX && startX <= endRoboX) && (endY >= startRoboY && endY <= startRoboY + robo.getHeight())) ||
                ((endX >= startRoboX && endX <= endRoboX) && (endY >= startRoboY && endY <= startRoboY + robo.getHeight()))
        ) return true;
        return false;
    }

    public void collidedTotem(){
        generateTotem();
        totemCount++;
    }

    public void onMudCollision(){
        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.splash);
        mediaPlayer.start();
    }

    protected void stop() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                playButton.setVisibility(View.VISIBLE);
                if (totemCount == 0){
                    playButton.setText("RESTART");
                    zapis(Integer.parseInt(String.valueOf(scoreCount / 15)), scoreCount);
                    playButton.setVisibility(View.VISIBLE);
                    firstGen = true;
                    if (timer != null) {
                        timer.cancel();
                        right = true;
                        isMoving = false;
                    }
                } else {
                    playButton.setText("Continue ( " + totemCount + " )");
                    playButton.setVisibility(View.VISIBLE);
                    if (timer != null) {
                        timer.cancel();
                        right = true;
                        isMoving = false;
                    }
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        stop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stop();
    }
    @Override
    protected void onStart() {
        super.onStart();
        checkInternet();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null){
            startActivity(new Intent(Game.this, LoginActivity.class));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkInternet();
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }


    public static boolean inRange(float value, float min, float max) {
        return toRange(value, min, max) == value;
    }

    public static float toRange(float value, float min, float max) {
        return Math.max(Math.min(max, value), min);
    }

    public int dpToPx(int dp) {
        float density = this.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

    public void zapis(int ball, int scoreCount){
        Query phoneQuery = data.getDatabaseReference().orderByChild("name").equalTo(user.getEmail());
        phoneQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    Player z = singleSnapshot.getValue(Player.class);
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("name", z.getName());
                    if (Integer.parseInt(z.getScore()) < scoreCount){
                        hashMap.put("score" , String.valueOf(scoreCount));
                    }
                    int ballance = Integer.parseInt(z.getBallance()) + ball;
                    hashMap.put("ballance" , String.valueOf(ballance));
                    data.update(z.getNickname(), hashMap);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Not GUT");
            }
        });
    }
}