package com.example.smellgood;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class Game extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        setContentView(R.layout.game_layout);
    }


    public void onBackButton(View view){
        Intent intent = new Intent(Game.this, Main.class);
        startActivity(intent);
    }


    /*public void setTimer(){
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
    }*/




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