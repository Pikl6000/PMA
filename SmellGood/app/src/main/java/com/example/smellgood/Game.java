package com.example.smellgood;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Game extends AppCompatActivity {

    private Handler handler=new Handler();
    private Timer timer;
    private SharedPreferences settings;
    private ImageView robo, roboDeadRight, roboStand, roboToRight, mud, powder;
    private Button play;
    private TextView score, nadpis, topBody;
    private RelativeLayout displej;
    private int speedMud, speedRobo, period, round, body, tBody;
    private float roboX, mudY, powderY;
    private boolean up=false, boolPowder=false, prvyBod=true;
    private Random rd=new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
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
        if (up){
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
}