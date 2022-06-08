package com.example.smellgood;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Random;
import java.util.Timer;

public class Main extends AppCompatActivity {
    FirebaseAuth mAuth;
    public static Fdata data;

    private Handler handler=new Handler();
    private Timer timer;
    private SharedPreferences settings;
    private ImageView robo, roboDeadRight, roboStand, roboToRight, mud, powder;
    private Button play;
    private TextView score, totem;
    private RelativeLayout displej;
    private int speedMud, speedRobo, period, round, body, tBody;
    private float roboX, mudY, powderY;
    private boolean up=false, boolPowder=false, prvyBod=true;
    private Random rd=new Random();
    private MediaPlayer mp;
    private ImageButton profile;

    public static int roboid = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        setContentView(R.layout.activity_menu);
        checkInternet();

        mAuth = FirebaseAuth.getInstance();
        data = new Fdata();


        mp = MediaPlayer.create(this, R.raw.main);
        mp.setLooping(true);
        mp.start();

        profile = findViewById(R.id.profileButton);
    }
    public void checkInternet(){
        if (!isNetworkAvailable()){
            startActivity(new Intent(Main.this, NoInternet.class));
        }
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void onStartButton(View view){
        mp.reset();
        Intent intent = new Intent(Main.this, Game.class);
        startActivity(intent);
    }
    public void onScoreButton(View view){
        mp.reset();
        Intent intent = new Intent(Main.this, ScoreBoard.class);
        startActivity(intent);
    }
    public void onExitButton(View view){
        mp.reset();
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory( Intent.CATEGORY_HOME );
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }
    public void openProfile(View view){
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null){
            startActivity(new Intent(Main.this, Profile.class));
        }
        else {
            startActivity(new Intent(Main.this, LoginActivity.class));
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mp.reset();
        checkInternet();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mp.setLooping(true);
        mp.start();
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
}