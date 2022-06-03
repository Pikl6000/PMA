package com.example.smellgood;

import android.content.Intent;
import android.graphics.drawable.Drawable;
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
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {
    EditText etRegEmail,etRegPassword,password1,nickname;
    TextView tvLoginHere,back;
    Button btnRegister;
    boolean good;
    Fdata data;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        data = Main.data;

        etRegEmail = findViewById(R.id.mail);
        etRegPassword = findViewById(R.id.password);
        tvLoginHere = findViewById(R.id.tvLoginHere);
        btnRegister = findViewById(R.id.register);
        password1 = findViewById(R.id.password1);
        back = findViewById(R.id.goBack5);
        nickname = findViewById(R.id.nick);

        back.setOnClickListener(view -> {
            startActivity(new Intent(this,Main.class));
        });

        mAuth = FirebaseAuth.getInstance();

        btnRegister.setOnClickListener(view ->{
            createUser();
        });

        tvLoginHere.setOnClickListener(view ->{
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        });
    }

    private void createUser(){
        good = true;
        String email = etRegEmail.getText().toString(),nick = nickname.getText().toString();
        String password = etRegPassword.getText().toString(),passworD = password1.getText().toString();

        if (TextUtils.isEmpty(email)){
            etRegEmail.setError("Email cannot be empty");
            etRegEmail.requestFocus();
        }else if(TextUtils.isEmpty(nick)){
            nickname.setError("Nickname cannot be empty");
            nickname.requestFocus();
        }else if(nick.length() < 4){
            nickname.setError("Nickname must be at least 3 characters");
            nickname.requestFocus();
        }else if(nick.length() > 15){
            nickname.setError("Nickname max length is 16  characters");
            nickname.requestFocus();
        } else if (TextUtils.isEmpty(password)){
            etRegPassword.setError("Password cannot be empty");
            etRegPassword.requestFocus();
        }else if (TextUtils.isEmpty(passworD)){
            password1.setError("Password cannot be empty");
            password1.requestFocus();
        }
        else if (password.length() < 6){
            etRegPassword.setError("Password must be at least 6 characters");
            etRegPassword.requestFocus();
        }
        else if (passworD.length() < 6){
            password1.setError("Password must be at least 6 characters");
            password1.requestFocus();
        }
        else if (!password.equals(passworD)) {
            password1.setError("Passwords must be the same");
            password1.requestFocus();
        }
        else{
            Player p = new Player(email,nick,"0","1","0","");
            data.getDatabaseReference().addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot dataP: dataSnapshot.getChildren()){
                        if (dataP.getKey().equals(nick)) {
                            Toast.makeText(RegisterActivity.this, "User with this nickname already exist", Toast.LENGTH_SHORT).show();
                            nickname.setError("Nickname taken");
                            nickname.requestFocus();
                            good = false;
                        }
                    }
                    if (good){
                        Profile.userId = nick;
                        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(RegisterActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();

                                    data.add(p).addOnSuccessListener(suc->{
                                        Toast.makeText(RegisterActivity.this, "success", Toast.LENGTH_SHORT).show();
                                    }).addOnFailureListener(er->{
                                        Toast.makeText(RegisterActivity.this, "Not GUT", Toast.LENGTH_SHORT).show();
                                    });
                                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                }else{
                                    Toast.makeText(RegisterActivity.this, "Email address already taken", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
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
