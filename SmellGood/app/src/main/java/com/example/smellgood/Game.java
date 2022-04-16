package com.example.smellgood;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
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
    private ImageView robo, roboDeadRight, roboStand, roboToRight, mud, powder, bottom;
    private Button play;
    private TextView score, totem;
    private LinearLayout gamePanel;
    private int speedMud, speedRobo, period, round, body, tBody,totemB,scoreB;
    private float roboX, mudY, powderY, sirka,vyska;
    private boolean right = false, boolPowder = false, prvyBod = true , hybeSa = false, firstGen = true;
    private boolean left = false, prvaZmena = true;
    private Random rd = new Random();
    private int[] robko = {R.drawable.robostand,R.drawable.robostandl};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        setContentView(R.layout.game_layout);

        period = 1;

        scoreB = 0;
        totemB = 0;

        robo = findViewById(R.id.robo);
        totem = findViewById(R.id.totem);
        score = findViewById(R.id.score);
        gamePanel = findViewById(R.id.gamePanel);
        play = findViewById(R.id.startButton);
        mud = findViewById(R.id.mud);
        powder = findViewById(R.id.powder);
        bottom = findViewById(R.id.bottom);

        gamePanel.post(new Runnable() {
            public void run() {
                sirka = gamePanel.getWidth();
                vyska = gamePanel.getHeight() - bottom.getHeight() - mud.getHeight();
            }
        });

        right = true;
    }

    public void klikloSa(View view){
        score.setText("Score : 0");
        totem.setText("Totem : 0");
        play.setVisibility(View.GONE);
        generateMud();
        generatePowder();
        setTimer();
    }

    public void updateText(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                score.setText("Score : "+scoreB);
                totem.setText("Totem : "+totemB);
            }
        });
    }

    public void setTimer(){
        if (hybeSa){
            return;
        }
        else {
            hybeSa = true;
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    pohyb();
                    pohybMud();
                    pohybPowder();
                    updateText();
                }}, 0, period);
        }
    }


    public void pohyb(){
        //pohyb hracej postavy
        if (hybeSa){
            if (right){
                if (prvaZmena){
                    prvaZmena = false;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            robo.setImageResource(robko[0]);
                        }
                    });
                }
                if (roboX+robo.getWidth()>gamePanel.getWidth()){
                    roboX=gamePanel.getWidth()-robo.getWidth();
                    robo.setX(roboX);
                    prvaZmena = true;
                }
                else {
                    roboX+=1;
                    robo.setX(roboX);
                }
            }else {
                if (prvaZmena){
                    prvaZmena = false;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            robo.setImageResource(robko[1]);
                        }
                    });
                }
                if ((roboX-1)<0){
                    roboX=0;
                    robo.setX(roboX);
                    prvaZmena = true;
                }
                else {
                    roboX-=1;
                    robo.setX(roboX);
                }
            }
        }
    }


    public void pohybMud(){
        if (hybeSa){
            mudY += 1.5;
            mud.setY(mudY);
            dotykajuSaMud();
            if (mudY >= vyska){
                generateMud();
            }
        }
    }

    public void pohybPowder(){
        if (hybeSa){
            powderY += 1.5;
            powder.setY(powderY);
            dotykajuSaPowder();
        }
    }

    public void klik(View view){
        prvaZmena = true;
        if (right){
            right = false;
        }
        else right = true;
    }

    /* generovanie objektov, ktorym sa treba vyhybat */
    public void generateMud(){
        mud.setY(0);
        mud.setX((float)Math.random()*(sirka-mud.getWidth()));
        mud.setVisibility(View.VISIBLE);
        mudY = mud.getY();
    }

    /* generovanie objektov, ktore treba zbierat */
    public void generatePowder(){
        powder.setVisibility(View.VISIBLE);
        if (firstGen){
            powder.setY(-500);
            firstGen = !firstGen;
        } else {
            powder.setY(0);
        }
        float mudStart = mud.getX(), mudEnd = mudStart + mud.getWidth();
        float powderStart = powder.getX(), powderEnd = powderStart + powder.getWidth();
        while ((powderStart <= mudStart && powderEnd >= mudStart) || (powderStart <= mudEnd && powderEnd >= mudEnd)) {
            powder.setX((float)Math.random()*(sirka-powder.getWidth()));
            powderStart = powder.getX();
            powderEnd = powderStart + powder.getWidth();
        }
        powder.setX((float)Math.random()*(sirka-powder.getWidth()));
        powder.setVisibility(View.VISIBLE);
        powderY = powder.getY();
    }

    private void dotykajuSaMud(){
        Rect myViewRect = new Rect();
        robo.getHitRect(myViewRect);
        Rect otherViewRect1 = new Rect();
        mud.getHitRect(otherViewRect1);
        if (Rect.intersects(myViewRect,otherViewRect1)){
            stop();
        }
    }

    private void dotykajuSaPowder(){
        Rect myViewRect = new Rect();
        robo.getHitRect(myViewRect);
        Rect otherViewRect1 = new Rect();
        powder.getHitRect(otherViewRect1);
        if (Rect.intersects(myViewRect,otherViewRect1)){
            generatePowder();
            scoreB++;
        }
        if (powder.getY() >= vyska){
            generatePowder();
        }
    }

    protected void stop(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                play.setVisibility(View.VISIBLE);
                play.setText("RESTART");
                scoreB = 0;
                totemB = 0;
                firstGen = true;
                if (timer != null) {
                    timer.cancel();
                    right = true;
                    hybeSa = false;
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
    protected void onResume() {
        super.onResume();
        if (timer != null){

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