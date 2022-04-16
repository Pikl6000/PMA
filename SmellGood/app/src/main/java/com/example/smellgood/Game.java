package com.example.smellgood;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Game extends AppCompatActivity {
    private Handler handler=new Handler();
    private Timer timer;
    private SharedPreferences settings;
    private ImageView robo, roboDeadRight, roboStand, roboToRight, mud, powder;
    private Button play;
    private TextView score, totem;
    private LinearLayout gamePanel;
    private int speedMud, speedRobo, period, round, body, tBody;
    private float roboX, mudY, powderY;
    private boolean right = false, boolPowder = false, prvyBod = true , hybeSa = false;
    private Random rd=new Random();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        setContentView(R.layout.game_layout);

        period = 1;

        robo = findViewById(R.id.robo);
        totem = findViewById(R.id.totem);
        score = findViewById(R.id.score);
        gamePanel = findViewById(R.id.gamePanel);
        play = findViewById(R.id.startButton);

        right = true;
    }

    public void klikloSa(View view){
        score.setText("Score : 0");
        totem.setText("Totem : 0");
        hybeSa = true;
        play.setVisibility(View.GONE);
        setTimer();
    }

    public void setTimer(){
        hybeSa = true;
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                pohyb();
            }}, 0, period);
    }


    public void pohyb(){
        //pohyb hracej postavy
        if (hybeSa){
            if (right){
                roboX+=1;
                robo.setX(roboX);
                if (roboX+robo.getWidth()>gamePanel.getWidth()){
                    roboX=gamePanel.getWidth()-robo.getWidth();
                    robo.setX(roboX);
                }

            }else {
                roboX-=1;
                robo.setX(roboX);
                if (roboX<0){
                    roboX=0;
                    robo.setX(roboX);
                }
            }
        }

        /*//pohyb prekazok
        mudY-=speedMud;
        mud.setX(mudY);

        if (mudY+mud.getWidth()<=0){
            mudY=gamePanel.getWidth();
            mud.setX(mudY);
            round++;
            if (round%3==0){
                speedMud++;
                boolPowder=true;
                setPowderY();
                powder.setVisibility(View.VISIBLE);
            }
            generateMud();
        }*/
    }

    public void klik(View view){
        if (right){
            right = false;
        }
        else right = true;
    }

//    public void generateMud(){
//        ImageView imageView = new ImageView(this);
//
//        Bitmap bmp;
//        bmp = BitmapFactory.decodeResource(getResources(),R.drawable.mud);
//        bmp = Bitmap.createScaledBitmap(bmp,150,150,true);
//        imageView.setImageBitmap(bmp);
//        imageView.setY(-1000);
//        imageView.setX(50);
//
//        gamePanel.addView(imageView);
//    }

    public void setPowderY(){
        int random=rd.nextInt(gamePanel.getHeight()-powder.getHeight());
        powder.setY(random);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (timer != null) {
            timer.cancel();
            right = true;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (timer != null){
            timer.c
        }
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
}