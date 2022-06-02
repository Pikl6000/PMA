package com.example.smellgood;
import android.content.AsyncQueryHandler;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smellgood.provider.NoteContentProvider;
import com.example.smellgood.provider.Provider;

import java.util.Timer;
import java.util.TimerTask;

import static com.example.smellgood.Defaults.NO_COOKIE;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Game extends AppCompatActivity {
    FirebaseAuth mAuth;
    private Timer timer;
    private ImageView robo, mud, powder, bottom, totem;
    private Button playButton, saveButton;
    private TextView scoreText, totemText;
    private LinearLayout gamePanel;
    private int period, totemCount, scoreCount, media_length;
    private float roboX, mudY, powderY,totemY, sirka, vyska;
    private boolean right = false, isMoving = false, firstGen = true;
    private boolean firstChange = true, roboRight;
    private int[] listOfImages;
    private static final int INSERT_NOTE_TOKEN = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        setContentView(R.layout.game_layout);
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();
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
        saveButton = findViewById(R.id.saveButton);
        totem = findViewById(R.id.totemObject);
        right = true;
        updateRobo();
    }

    //MENENIE ROBA , TREBA DOROBIT PODLA POSLEDNEHO POCTU VARIACII
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

    public void klikloSa(View view) {
        sirka = gamePanel.getWidth();
        vyska = gamePanel.getHeight() - bottom.getHeight() - mud.getHeight();
        if (totemCount > 0){
            totemCount--;
        } else {
            scoreCount = 0;
            totemCount = 0;
        }
        scoreText.setText("Score : " + scoreCount);
        totemText.setText("Totem : " + totemCount);
        saveButton.setVisibility(View.GONE);
        playButton.setVisibility(View.GONE);
        robo.setImageResource(listOfImages[0]);
        robo.setLayoutParams(new LinearLayout.LayoutParams(dpToPx(85), dpToPx(140)));
        generateMud();
        generatePowder();
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
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    moveRobo();
                    pohybMud();
                    pohybPowder();
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
                        if (roboX + robo.getWidth() > gamePanel.getWidth()) {
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

    public void pohybMud() {
        if (isMoving) {
            mudY += 1.35;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mud.setY(mudY);
                    dotykajuSaMud();
                }
            });
            if (mudY >= vyska) {
                generateMud();
            }
        }
    }

    public void pohybPowder() {
        if (isMoving) {
            powderY += 1.35;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    powder.setY(powderY);
                    dotykajuSaPowder();
                }
            });
        }
    }

    public void pohybTotem() {
        if (isMoving) {
            totemY += 1.35;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    totem.setY(totemY);
                    dotykajuSaTotem();
                }
            });
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
        mud.setY(-40);
        mud.setX((float) Math.random() * (sirka - mud.getWidth()));
        mudY = mud.getY();
        mud.setVisibility(View.VISIBLE);
    }

    /* generovanie objektov, ktore treba zbierat */
    public void generatePowder() {
        if (firstGen) {
            powder.setY(-500);
            firstGen = !firstGen;
        } else {
            powder.setY(-40);
        }
        float mudStart = mud.getX(), mudEnd = mudStart + mud.getWidth();
        float powderStart = powder.getX(), powderEnd = powderStart + powder.getWidth();
        do {
            powder.setX((float) Math.random() * (sirka - powder.getWidth()));
            powderStart = powder.getX();
            powderEnd = powderStart + powder.getWidth();
            mudStart = mud.getX();
            mudEnd = mudStart + mud.getWidth();
        } while (inRange(powderStart, mudStart, mudEnd) || inRange(powderEnd, mudStart, mudEnd));
        powderY = powder.getY();
        powder.setVisibility(View.VISIBLE);
    }

    public void generateTotem() {

        float mudStart = mud.getX(), mudEnd = mudStart + mud.getWidth();
        float powderStart = powder.getX(), powderEnd = powderStart + powder.getWidth();
        float totemStart = totem.getX(), totemEnd = totemStart + totem.getWidth();
        do {
            totem.setX((float) Math.random() * (sirka - powder.getWidth()));
            powderStart = powder.getX();
            powderEnd = powderStart + powder.getWidth();
            mudStart = mud.getX();
            mudEnd = mudStart + mud.getWidth();
            totemStart = totem.getX();
            totemEnd = totemStart + totem.getWidth();
        } while (inRange(totemStart, mudStart, mudEnd) ||
                inRange(totemEnd, mudStart, mudEnd) ||
                inRange(totemStart, powderStart, powderEnd) ||
                inRange(totemEnd, powderStart, powderEnd)
        );

        totem.setY(-40);
        totem.setVisibility(View.VISIBLE);
        totemY = totem.getY();
    }

    private void dotykajuSaMud() {
        Rect myViewRect = new Rect();
        robo.getHitRect(myViewRect);
        Rect otherViewRect1 = new Rect();
        mud.getHitRect(otherViewRect1);
        if (Rect.intersects(myViewRect, otherViewRect1)) {
            stop();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (roboRight) {
                        robo.setImageResource(listOfImages[2]);
                    } else{
                        robo.setImageResource(listOfImages[3]);
                    }
                    if (robo.getX() + robo.getWidth() >= sirka) {
                        roboX = sirka - robo.getWidth();
                        robo.setX(roboX);
                    }
                    robo.setLayoutParams(new LinearLayout.LayoutParams(dpToPx(140), dpToPx(85)));
                    powder.setVisibility(View.GONE);
                    mud.setVisibility(View.GONE);
                    totem.setVisibility(View.GONE);
                }
            });
//            mpEffects = MediaPlayer.create(Game.this, R.raw.splash);
//            mpEffects.start();
        }
    }

    private void dotykajuSaPowder() {
        Rect myViewRect = new Rect();
        robo.getHitRect(myViewRect);
        Rect otherViewRect1 = new Rect();
        powder.getHitRect(otherViewRect1);
        if (Rect.intersects(myViewRect, otherViewRect1)) {
            generatePowder();
            scoreCount++;
//            mpEffects = MediaPlayer.create(this, R.raw.scoreText);
//            mpEffects.start();
        }
        if (powder.getY() >= vyska) {
            generatePowder();
        }
    }

    private void dotykajuSaTotem() {
        Rect myViewRect = new Rect();
        robo.getHitRect(myViewRect);
        Rect otherViewRect1 = new Rect();
        totem.getHitRect(otherViewRect1);
        if (Rect.intersects(myViewRect, otherViewRect1)) {
            totem.setX(sirka - 1000);
            totemCount++;
//            mpEffects = MediaPlayer.create(this, R.raw.scoreText);
//            mpEffects.start();
        }
        if (totem.getY() >= vyska) {
            totem.setX(sirka - 1000);
        }
    }

    protected void stop() {
//        mp.reset();
//        mp = MediaPlayer.create(this, R.raw.dead);
//        mp.setLooping(true);
//        mp.start();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                saveButton.setVisibility(View.VISIBLE);
                playButton.setVisibility(View.VISIBLE);
                if (totemCount == 0){
                    playButton.setText("RESTART");
                    saveButton.setVisibility(View.VISIBLE);
                    playButton.setVisibility(View.VISIBLE);
                    firstGen = true;
                    if (timer != null) {
                        timer.cancel();
                        right = true;
                        isMoving = false;
                    }
                } else {
                    playButton.setText("Continue ( " + totemCount + " )");
                    saveButton.setVisibility(View.VISIBLE);
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
//        mp.reset();
//        mpEffects.reset();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stop();
//        mp.reset();
//        mpEffects.reset();
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null){
            startActivity(new Intent(Game.this, LoginActivity.class));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void createReport(View view) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null){
            startActivity(new Intent(Game.this, LoginActivity.class));
        }
        else{
            insertIntoContentProvider(user.getEmail().toString(), String.valueOf(scoreCount));
        }
    }


    private void insertIntoContentProvider(String nickname, String score) {
        Uri uri = NoteContentProvider.CONTENT_URI;
        ContentValues values = new ContentValues();
        values.put(Provider.Note.NICKNAME, nickname);
        values.put(Provider.Note.SCORE, score);
        AsyncQueryHandler insertHandler = new
                AsyncQueryHandler(getContentResolver()) {
                    @Override
                    protected void onInsertComplete(int token, Object cookie, Uri uri) {
                        Toast.makeText(Game.this, "Saved",
                                Toast.LENGTH_SHORT).show();
                    }
                };
        insertHandler.startInsert(INSERT_NOTE_TOKEN, NO_COOKIE, uri, values);
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
}