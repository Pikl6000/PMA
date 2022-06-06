package com.example.smellgood;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
    String[] unlocked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        setContentView(R.layout.shop);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        option1 = findViewById(R.id.shop1);
        option2 = findViewById(R.id.shop2);
        option3 = findViewById(R.id.shop3);
        option4 = findViewById(R.id.shop4);
        back = findViewById(R.id.goBack2);
        data = Main.data;
        ball = findViewById(R.id.ballanc);

        back.setOnClickListener(view -> {
            startActivity(new Intent(this,Main.class));
        });

        updateUI();

        option1.setOnClickListener(view -> {
            Toast.makeText(this, "Robo Changed", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, Profile.class));
        });
        option2.setOnClickListener(view -> {
            if (price2 == 0){
                Toast.makeText(this, "Robo Changed", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, Profile.class));
            }
            else {
                if (ballance >= price2){
                    ballance -= price2;
                    zapis(2);
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
                Toast.makeText(this, "Robo Changed", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, Profile.class));
            }
            else {
                if (ballance >= price3){
                    ballance -= price3;
                    zapis(3);
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
                Toast.makeText(this, "Robo Changed", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, Profile.class));
            }
            else {
                if (ballance >= price4){
                    ballance -= price4;
                    zapis(4);
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
                                }
                                if (a.equals("2")){
                                    option2.setText("SELECT");
                                    price2 = 0;
                                }
                                if (a.equals("3")){
                                    option3.setText("SELECT");
                                    price3 = 0;
                                }
                                if (a.equals("4")){
                                    option4.setText("SELECT");
                                    price4 = 0;
                                }
                            }
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