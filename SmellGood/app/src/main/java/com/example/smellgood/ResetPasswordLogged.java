package com.example.smellgood;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ResetPasswordLogged extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    EditText pass1,pass2;
    Button btnReset;
    TextView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        setContentView(R.layout.reset_pass_log);
        checkInternet();

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if (user == null){
            startActivity(new Intent(this, LoginActivity.class));
        }

        pass1 = findViewById(R.id.pass1);
        pass2 = findViewById(R.id.pass2);
        btnReset = findViewById(R.id.resetPass);
        back = findViewById(R.id.goBack);

        back.setOnClickListener(view -> {
            startActivity(new Intent(this,Main.class));
        });

        btnReset.setOnClickListener(view -> {
            String password1 = pass1.getText().toString(),password2 = pass2.getText().toString();

            if (TextUtils.isEmpty(password1)){
                pass1.setError("Password cannot be empty");
                pass1.requestFocus();
            }
            else if (TextUtils.isEmpty(password2)){
                pass2.setError("Password cannot be empty");
                pass2.requestFocus();
            }
            else if (password1.length() < 6){
                pass1.setError("Password must be at least 6 characters");
                pass1.requestFocus();
            }
            else if (password2.length() < 6){
                pass1.setError("Password must be at least 6 characters");
                pass1.requestFocus();
            }
            else if (!password1.equals(password2)) {
                pass2.setError("Passwords must be the same");
                pass2.requestFocus();
            }
            else {
                user.updatePassword(password1)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ResetPasswordLogged.this, "Password changed", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(ResetPasswordLogged.this, Profile.class));
                                } else {
                                    Toast.makeText(ResetPasswordLogged.this, "Failed to change password!", Toast.LENGTH_SHORT).show();
                                }
                            }});
            }
        });
    }

    public void checkInternet(){
        if (!isNetworkAvailable()){
            startActivity(new Intent(ResetPasswordLogged.this, NoInternet.class));
        }
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
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
