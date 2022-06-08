package com.example.smellgood;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
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

public class ResetPassword extends AppCompatActivity {
    private FirebaseAuth mAuth;
    EditText mail;
    Button btnReset;
    TextView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        setContentView(R.layout.reset_pass);
        checkInternet();

        mAuth = FirebaseAuth.getInstance();


        mail = findViewById(R.id.mail4);
        btnReset = findViewById(R.id.resetPass);
        back = findViewById(R.id.goBack1);

        back.setOnClickListener(view -> {
            startActivity(new Intent(this,Main.class));
        });

        btnReset.setOnClickListener(view -> {
            String email = mail.getText().toString();

            if (TextUtils.isEmpty(email)){
                mail.setError("Email cannot be empty");
                mail.requestFocus();
            }
            else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                mail.setError("Enter valid e-mail!");
                mail.requestFocus();
            }
            else {
                mAuth.getInstance().sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ResetPassword.this, "E-mail was sent", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(ResetPassword.this, Main.class));
                                } else {
                                    Toast.makeText(ResetPassword.this, "Failed to send reset E-mail!", Toast.LENGTH_SHORT).show();
                                }
                            }});
            }
        });
    }

    public void checkInternet(){
        if (!isNetworkAvailable()){
            startActivity(new Intent(ResetPassword.this, NoInternet.class));
        }
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
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
