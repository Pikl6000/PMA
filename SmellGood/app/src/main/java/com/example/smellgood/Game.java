package com.example.smellgood;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
    private RelativeLayout displej;
    private int speedMud, speedRobo, period, round, body, tBody;
    private float roboX, mudY, powderY;
    private boolean right = false, boolPowder = false, prvyBod = true;
    private Random rd=new Random();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        setContentView(R.layout.game_layout);

        robo = findViewById(R.id.robo);
        totem = findViewById(R.id.totem);
        score = findViewById(R.id.score);


    }


    public void onBackButton(View view){
        Intent intent = new Intent(Game.this, Main.class);
        startActivity(intent);
    }


    public void setTimer(){
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(() -> startGame());
            }}, 0, period);
    }

    public void startGame(){
        //pohyb hracej postavy
        if (right){
            roboX+=speedRobo;
            robo.setY(roboX);
            if (roboX+robo.getHeight()>displej.getHeight()){
                roboX=displej.getHeight()-robo.getHeight();
                robo.setY(roboX);
            }

        }else {
            roboX-=speedRobo;
            robo.setY(roboX);
            if (roboX<0){
                roboX=0;
                robo.setY(roboX);
            }
        }

        //pohyb prekazok
        mudY-=speedMud;
        mud.setX(mudY);

        if (mudY+mud.getWidth()<=0){
            mudY=displej.getWidth();
            mud.setX(mudY);
            round++;
            if (round%3==0){
                speedMud++;
                boolPowder=true;
                setPowderY();
                powder.setVisibility(View.VISIBLE);
            }
            generateMud();
        }
    }

    public void generateMud(){

    }

    public void setPowderY(){
        int random=rd.nextInt(displej.getHeight()-powder.getHeight());
        powder.setY(random);
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