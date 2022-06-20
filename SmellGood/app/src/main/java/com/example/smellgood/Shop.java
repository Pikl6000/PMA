package com.example.smellgood;

/* kollarcikm@spse-po.sk */

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Shop extends AppCompatActivity {
    Button option1,option2,option3,option4;
    TextView back,ball;
    Fdata data;
    FirebaseAuth mAuth;
    private FirebaseUser user;
    int ballance = 0 , price1 = 0 , price2 = 100 , price3 = 250 , price4 = 500;
    private TextView pr1,pr2,pr3,pr4;
    private TextView un1,un2,un3,un4;
    private ImageView coin1,coin2,coin3,coin4;
    private LinearLayout back1,back2,back3,back4;
    String[] unlocked;
    private MediaPlayer select;
    private MediaPlayer buy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        setContentView(R.layout.shop);
        checkInternet();

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        option1 = findViewById(R.id.shop1);
        option2 = findViewById(R.id.shop2);
        option3 = findViewById(R.id.shop3);
        option4 = findViewById(R.id.shop4);
        back = findViewById(R.id.goBack2);
        data = Main.data;
        ball = findViewById(R.id.ballanc);

        pr1 = findViewById(R.id.price1);
        pr2 = findViewById(R.id.price2);
        pr3 = findViewById(R.id.price3);
        pr4 = findViewById(R.id.price4);

        un1 = findViewById(R.id.un1);
        un2 = findViewById(R.id.un2);
        un3 = findViewById(R.id.un3);
        un4 = findViewById(R.id.un4);

        coin1 = findViewById(R.id.coin1);
        coin2 = findViewById(R.id.coin2);
        coin3 = findViewById(R.id.coin3);
        coin4 = findViewById(R.id.coin4);

        back1 = findViewById(R.id.backR1);
        back2 = findViewById(R.id.backR2);
        back3 = findViewById(R.id.backR3);
        back4 = findViewById(R.id.backR4);

        select = MediaPlayer.create(this, R.raw.select);
        buy = MediaPlayer.create(this, R.raw.buy);

        back.setOnClickListener(view -> {
            startActivity(new Intent(this,Main.class));
        });

        updateUI();

        option1.setOnClickListener(view -> {
            zapis(1);
            select.start();
            Toast.makeText(this, "Robo Changed", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, Profile.class));
        });
        option2.setOnClickListener(view -> {
            if (price2 == 0){
                zapis(2);
                select.start();
                Toast.makeText(this, "Robo Changed", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, Profile.class));
            }
            else {
                if (ballance >= price2){
                    ballance -= price2;
                    zapis(2);
                    buy.start();
                    Toast.makeText(this, "Robo purchased & equipped", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, Profile.class));
                }
                else {
                    Toast.makeText(this, "Not enough ballance", Toast.LENGTH_SHORT).show();
                }
            }
        });
        option3.setOnClickListener(view -> {
            if (price3 == 0){
                zapis(3);
                select.start();
                Toast.makeText(this, "Robo Changed", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, Profile.class));
            }
            else {
                if (ballance >= price3){
                    ballance -= price3;
                    zapis(3);
                    buy.start();
                    Toast.makeText(this, "Robo purchased & equipped", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, Profile.class));
                }
                else {
                    Toast.makeText(this, "Not enough ballance", Toast.LENGTH_SHORT).show();
                }
            }
        });
        option4.setOnClickListener(view -> {
            if (price4 == 0){
                zapis(4);
                select.start();
                Toast.makeText(this, "Robo Changed", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, Profile.class));
            }
            else {
                if (ballance >= price4){
                    ballance -= price4;
                    zapis(4);
                    buy.start();
                    Toast.makeText(this, "Robo purchased & equipped", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, Profile.class));
                }
                else {
                    Toast.makeText(this, "Not enough ballance", Toast.LENGTH_SHORT).show();
                }
            }
        });

        data.getDatabaseReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                updateUI();
            }
            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    public void checkInternet(){
        if (!isNetworkAvailable()){
            startActivity(new Intent(Shop.this, NoInternet.class));
        }
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void updateRobo(String robo){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (robo){
                    case "1":{
                        coin1.setVisibility(View.GONE);
                        pr1.setVisibility(View.GONE);
                        un1.setVisibility(View.VISIBLE);
                        un1.setText("SELECTED");
                        back1.setBackgroundResource(R.drawable.selectedrobo);
                        return;
                    }
                    case "2":{
                        coin2.setVisibility(View.GONE);
                        pr2.setVisibility(View.GONE);
                        un2.setVisibility(View.VISIBLE);
                        un2.setText("SELECTED");
                        back2.setBackgroundResource(R.drawable.selectedrobo);
                        return;
                    }
                    case "3":{
                        coin3.setVisibility(View.GONE);
                        pr3.setVisibility(View.GONE);
                        un3.setVisibility(View.VISIBLE);
                        un3.setText("SELECTED");
                        back3.setBackgroundResource(R.drawable.selectedrobo);
                        return;
                    }
                    case "4":{
                        coin4.setVisibility(View.GONE);
                        pr4.setVisibility(View.GONE);
                        un4.setVisibility(View.VISIBLE);
                        un4.setText("SELECTED");
                        back4.setBackgroundResource(R.drawable.selectedrobo);
                        return;
                    }
                    default:{
                        System.out.println("Ehmm, problem");
                        return;
                    }
                }
            }
        });
    }

    private void updateUI(){
        Query phoneQuery = data.getDatabaseReference().orderByChild("name").equalTo(user.getEmail());
        phoneQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    Player z = singleSnapshot.getValue(Player.class);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            assert z != null;
                            ball.setText(z.getBallance());
                            ballance = Integer.parseInt(z.getBallance());
                            unlocked = z.getUnlock().split(",");
                            for (String a : unlocked){
                                if (a.equals("1")){
                                    option1.setText("SELECT");
                                    coin1.setVisibility(View.GONE);
                                    un1.setVisibility(View.VISIBLE);
                                    un1.setText("UNLOCKED");
                                    pr1.setVisibility(View.GONE);
                                    un1.setVisibility(View.VISIBLE);
                                }
                                if (a.equals("2")){
                                    coin2.setVisibility(View.GONE);
                                    pr2.setVisibility(View.GONE);
                                    un2.setText("UNLOCKED");
                                    un2.setVisibility(View.VISIBLE);
                                    option2.setText("SELECT");
                                    price2 = 0;
                                }
                                if (a.equals("3")){
                                    coin3.setVisibility(View.GONE);
                                    pr3.setVisibility(View.GONE);
                                    un3.setText("UNLOCKED");
                                    un3.setVisibility(View.VISIBLE);
                                    option3.setText("SELECT");
                                    price3 = 0;
                                }
                                if (a.equals("4")){
                                    coin4.setVisibility(View.GONE);
                                    pr4.setVisibility(View.GONE);
                                    un4.setText("UNLOCKED");
                                    un4.setVisibility(View.VISIBLE);
                                    option4.setText("SELECT");
                                    price4 = 0;
                                }
                            }
                            updateRobo(z.getRobo());
                        }
                    });
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Not GUT");
            }
        });
    }


    public void zapis(int robko){
        Query phoneQuery = data.getDatabaseReference().orderByChild("name").equalTo(user.getEmail());
        phoneQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    Player z = singleSnapshot.getValue(Player.class);
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("name", z.getName());
                    hashMap.put("nickname", z.getNickname());
                    hashMap.put("score", z.getScore());
                    System.out.println("Robo = "+robko);
                    hashMap.put("robo", String.valueOf(robko));
                    hashMap.put("ballance", String.valueOf(ballance));

                    String u = z.getUnlock();
                    if (u.isEmpty()){
                        u = String.valueOf(robko) + ",";
                    }
                    else{
                        String [] un = u.split(",");
                        if (un.length > 1){
                            boolean unlo = false;
                            for (String a : un){
                                System.out.println("a = "+a);
                                if (a.equals(String.valueOf(robko))){
                                    unlo = true;
                                }
                            }
                            if (!unlo){
                                u += String.valueOf(robko) + ",";
                            }
                        }
                        else {
                            if (!un[0].equals(String.valueOf(robko))){
                                u += String.valueOf(robko) + ",";
                            }
                        }
                    }
                    z.setUnlock(u);
                    z.setRobo(String.valueOf(robko));
                    hashMap.put("unlock", z.getUnlock());

                    data.update(z.getNickname(), hashMap).addOnSuccessListener(suc -> {
                        finish();
                    });
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Not GUT");
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        checkInternet();
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
}