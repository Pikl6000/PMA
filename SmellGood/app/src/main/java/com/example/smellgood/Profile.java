package com.example.smellgood;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Profile extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private TextView nickname,score,logout,changeP,back,nick,ball;
    private Button change;
    private ImageView robko;
    private Fdata data;
    private int i = 1;
    public static String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        setContentView(R.layout.player_profile);
        checkInternet();

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if (user == null){
            startActivity(new Intent(Profile.this, LoginActivity.class));
        }

        data = Main.data;

        nickname = findViewById(R.id.nickname);
        score = findViewById(R.id.scoreProfile);
        change = findViewById(R.id.change);
        logout = findViewById(R.id.logout);
        robko = findViewById(R.id.robko);
        changeP = findViewById(R.id.changepass);
        back = findViewById(R.id.goBack3);
        nick = findViewById(R.id.nickP);
        ball = findViewById(R.id.ball);

        back.setOnClickListener(view -> {
            startActivity(new Intent(this,Main.class));
        });
        logout.setOnClickListener(view -> {
            mAuth.signOut();
            startActivity(new Intent(this, Main.class));
        });
        change.setOnClickListener(view -> {
            startActivity(new Intent(this, Shop.class));
        });
        changeP.setOnClickListener(view ->{
            startActivity(new Intent(this, ResetPasswordLogged.class));
        });
        updateUI();
        updateRobo();

        data.getDatabaseReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                updateUI();
                updateRobo();
            }
            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }

    public void checkInternet(){
        if (!isNetworkAvailable()){
            startActivity(new Intent(Profile.this, NoInternet.class));
        }
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void updateRobo(){
        int id = Main.roboid;
        if (id == 1){
            robko.setImageResource(R.drawable.robostand);
        }
        if (id == 2){
            robko.setImageResource(R.drawable.robostandpink);
        }
        if (id == 3){
            robko.setImageResource(R.drawable.robostandblue);
        }
        if (id == 4){
            robko.setImageResource(R.drawable.robostandwhite);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkInternet();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null){
            startActivity(new Intent(Profile.this, LoginActivity.class));
        }
        updateUI();
        updateRobo();
    }
    @Override
    protected void onResume() {
        super.onResume();
        checkInternet();
        updateUI();
        updateRobo();
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
                            nickname.setText(z.getName());
                            score.setText(z.getScore());
                            nick.setText(z.getNickname());
                            ball.setText(z.getBallance());
                        }
                    });
                    int r = Integer.parseInt(z.getRobo());
                    Main.roboid = r;
                    updateRobo();
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
