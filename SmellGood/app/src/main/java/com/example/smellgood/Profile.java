package com.example.smellgood;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class Profile extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseDatabase data;
    private DatabaseReference reference;
    private TextView nickname,score,logout;
    private Button change;
    private ImageView robko;
    private int i = 1;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        setContentView(R.layout.player_profile);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if (user == null){
            startActivity(new Intent(Profile.this, LoginActivity.class));
        }

        nickname = findViewById(R.id.nickname);
        score = findViewById(R.id.scoreProfile);
        change = findViewById(R.id.change);
        logout = findViewById(R.id.logout);
        robko = findViewById(R.id.robko);

        logout.setOnClickListener(view -> {
            mAuth.signOut();
            startActivity(new Intent(this, Main.class));
        });
        change.setOnClickListener(view -> {
            //startActivity(new Intent(this,));
            Toast.makeText(this, "Treba dorobiť", Toast.LENGTH_SHORT).show();
            if (i <= 2) i++;
            else i = 1;
            Main.roboid = i;
            updateRobo();
        });

        updateRobo();
        nacitanie();
    }

    private void nacitanie(){
        String mail = user.getEmail();
        nickname.setText(mail);

    }
    public void updateRobo(){
        int id = Main.roboid;
        if (id == 1){
            robko.setImageResource(R.drawable.robostand);
        }
        if (id == 2){
            robko.setImageResource(R.drawable.robostand2);
        }
        if (id == 3){
            robko.setImageResource(R.drawable.robostand3);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null){
            startActivity(new Intent(Profile.this, LoginActivity.class));
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
