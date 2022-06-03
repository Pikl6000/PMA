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
    TextView back;
    Fdata data;
    FirebaseAuth mAuth;
    private FirebaseUser user;

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

        back.setOnClickListener(view -> {
            startActivity(new Intent(this,Main.class));
        });

        option1.setOnClickListener(view -> {
            Main.roboid = 1;
            zapis(1);
            Toast.makeText(this, "Robo Changed", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, Profile.class));
        });
        option2.setOnClickListener(view -> {
            Main.roboid = 2;
            zapis(2);
            Toast.makeText(this, "Robo Changed", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, Profile.class));
        });
        option3.setOnClickListener(view -> {
            Main.roboid = 3;
            zapis(3);
            Toast.makeText(this, "Robo Changed", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, Profile.class));
        });
        option4.setOnClickListener(view -> {
            Main.roboid = 4;
            zapis(4);
            Toast.makeText(this, "Robo Changed", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, Profile.class));
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
                    System.out.println("rOBKO1 : "+robko);
                    hashMap.put("robo", String.valueOf(robko));
                    hashMap.put("ballance", z.getBallance());

                    String u = z.getUnlock();
                    if (u.isEmpty()){
                        u = String.valueOf(robko) + ",";
                    }
                    else{
                        String [] un = u.split(",");
                        if (un.length > 1){
                            boolean unlo = false;
                            for (int i = 0; i < un.length; i++) {
                                if (un[i].equals(String.valueOf(robko))){
                                    unlo = true;
                                    Toast.makeText(Shop.this, "Robo is already unlocked", Toast.LENGTH_SHORT).show();
                                }
                            }
                            if (!unlo){
                                u += String.valueOf(robko) + ",";
                            }
                        }
                        else {
                            if (un.length == 1 && un[0].equals(String.valueOf(robko))){
                                Toast.makeText(Shop.this, "Robo is already unlocked", Toast.LENGTH_SHORT).show();
                            }
                            else {
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