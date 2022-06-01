package com.example.smellgood;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.AsyncQueryHandler;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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
    private AnimatorSet animations;
    private Animator mudAnimator, powderAnimator, totemAnimator;
    private Timer timer;
    private ImageView robo, mud, powder, bottom, totem;
    private Button playButton, saveButton;
    private TextView scoreText, totemText;
    private LinearLayout gamePanel;
    private int period, totemCount, scoreCount, media_length;
    private float roboX, toFall;
    private boolean right = false, isMoving = false, firstGen = true;
    private boolean firstChange = true, roboRight, firstMud, firstPowder, firstTotem, stopAnimation;
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

        firstMud = true;
        firstPowder = true;
        firstTotem = true;
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

        mud.setVisibility(View.GONE); powder.setVisibility(View.GONE); totem.setVisibility(View.GONE);
        mud.setY(-100); powder.setY(-100); totem.setY(-100);

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
        scoreText.setText("Score : " + scoreCount);
        if (totemCount > 0){
            totemCount--;
        } else {
            scoreCount = 0;
            totemCount = 0;
        }
        totemText.setText("Totem : " + totemCount);
        saveButton.setVisibility(View.GONE);
        playButton.setVisibility(View.GONE);
        robo.setImageResource(listOfImages[0]);
        robo.setLayoutParams(new LinearLayout.LayoutParams(dpToPx(85), dpToPx(140)));
        setTimer();
        toFall = gamePanel.getBottom();
        animations = new AnimatorSet();
        animations.playTogether(
                fallAnimation(mud, 500, powder, totem, false, false),
                fallAnimation(powder, 1000, mud, totem, false, true),
                fallAnimation(totem, 20000, mud, powder, true, false)
        );
        animations.start();
        stopAnimation = false;

        animations.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                for (Animator animatorSet : animations.getChildAnimations()) {
                    animatorSet.cancel();
                }
            }
        });
    }

    public Animator fallAnimation(
            ImageView img,
            int postDelay,
            ImageView img1,
            ImageView img2,
            boolean isTotem,
            boolean isPowder
    ){
        ObjectAnimator animator = ObjectAnimator.ofFloat(img, "y", toFall);
        if (!isPowder && !isTotem) animator.setDuration(1800);
        else animator.setDuration(2500);
        animator.setStartDelay(postDelay);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (isTotem) firstTotem = true;
                else if (isPowder) firstPowder = true;
                else firstMud = true;
                img.setX(generateX(img, img1, img2));
                img.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                int delay;

                if (isTotem) delay = 20000;
                else if (isPowder) delay = (int) (Math.random()*1500);
                else delay = 0;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!stopAnimation){
                            animator.setStartDelay(delay);
                            if (scoreCount % 100 == 0 && !isPowder && !isTotem){
                                animator.setDuration(animator.getDuration() - 50);
                            }
                            animator.start();
                        }
                    }
                }, 0);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                animator.setRepeatCount(0);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        return animator;
    }

    public float generateX(ImageView generatedImg, ImageView img1, ImageView img2){
        float x;
        float [] img1Range = new float[2];
        float [] img2Range = new float[2];
        img1Range[0] = img1.getX() - img1.getWidth();
        img1Range[1] = img1.getX() + img1.getWidth();
        img2Range[0] = img2.getX() - img2.getWidth();
        img2Range[1] = img2.getX() + img2.getWidth();

        do {
            x = (float) (Math.random() * (gamePanel.getWidth() - generatedImg.getWidth()));
        } while ((x >= img1Range[0] && x <= img1Range[1]) || (x >= img2Range[0] && x <= img2Range[1]));
        return x;
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
                    pohyb();
                    updateText();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (collisionMud() && mud.getVisibility() != View.GONE){
                                if (firstMud){
                                    stop();
                                    firstMud = false;
                                }
                                mud.setVisibility(View.INVISIBLE);
                                System.out.println("mud");
                            }
                            if (collisionPowder() && powder.getVisibility() != View.GONE){
                                if (firstPowder){
                                    scoreCount += 10;
                                    firstPowder = false;
                                }
                                powder.setVisibility(View.INVISIBLE);
                                System.out.println("powder");
                            }
                            if (collisionTotem() && totem.getVisibility() != View.GONE){
                                if (firstTotem){
                                    totemCount++;
                                    firstTotem = false;
                                }
                                totem.setVisibility(View.INVISIBLE);
                                System.out.println("powder");
                            }
                        }
                    });
                }
            }, 0, period);
        }
    }

    public void pohyb() {
        //pohyb hracej postavy
        if (isMoving) {
            if (right) {
                if (firstChange) {
                    firstChange = false;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            robo.setImageResource(listOfImages[0]);
                            roboRight = true;
                        }
                    });
                }
                if (roboX + robo.getWidth() >= gamePanel.getWidth()) {
                    roboX = gamePanel.getWidth() - robo.getWidth();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            robo.setX(roboX);
                        }
                    });
                    firstChange = true;
                } else {
                    roboX += 1;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            robo.setX(roboX);
                        }
                    });
                }
            } else {
                if (firstChange) {
                    firstChange = false;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            robo.setImageResource(listOfImages[1]);
                            roboRight = false;
                        }
                    });
                }
                if ((roboX - 1) < 0) {
                    roboX = 0;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            robo.setX(roboX);
                        }
                    });
                    firstChange = true;
                } else {
                    roboX -= 1;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            robo.setX(roboX);
                        }
                    });
                }
            }
        }
    }

    public boolean collisionMud() {
        float startX = mud.getX();
        float endX = startX + mud.getWidth();
        float endY = mud.getY() - mud.getHeight();
        float startRoboX = robo.getX();
        float endRoboX = startRoboX + robo.getWidth();
        float startRoboY = robo.getY();

        if (((startX >= startRoboX && startX <= endRoboX) && (endY >= startRoboY && endY <= startRoboY + robo.getHeight())) ||
            ((endX >= startRoboX && endX <= endRoboX) && (endY >= startRoboY && endY <= startRoboY + robo.getHeight()))
        ) return true;
        return false;
    }

    public boolean collisionPowder() {
        float startX = powder.getX();
        float endX = startX + powder.getWidth();
        float endY = powder.getY() - powder.getHeight();
        float startRoboX = robo.getX();
        float endRoboX = startRoboX + robo.getWidth();
        float startRoboY = robo.getY();

        if (((startX >= startRoboX && startX <= endRoboX) && (endY >= startRoboY && endY <= startRoboY + robo.getHeight())) ||
            ((endX >= startRoboX && endX <= endRoboX) && (endY >= startRoboY && endY <= startRoboY + robo.getHeight()))
        ) return true;
        return false;
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

    public void klik(View view) {
        firstChange = true;
        if (right) {
            right = false;
        } else right = true;
    }



    protected void stop() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                stopAnimation = true;
                animations.cancel();
                mud.setY(-100); powder.setY(-100); totem.setY(-100);
                mud.setVisibility(View.GONE); powder.setVisibility(View.GONE); totem.setVisibility(View.GONE);
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