package com.example.smellgood;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class Shop extends AppCompatActivity {
    Button option1,option2,option3,option4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        setContentView(R.layout.shop);

        option1 = findViewById(R.id.shop1);
        option2 = findViewById(R.id.shop2);
        option3 = findViewById(R.id.shop3);
        option4 = findViewById(R.id.shop4);

        option1.setOnClickListener(view -> {
            Main.roboid = 1;
            Toast.makeText(this, "Robo Changed", Toast.LENGTH_SHORT).show();
        });
        option2.setOnClickListener(view -> {
            Main.roboid = 2;
            Toast.makeText(this, "Robo Changed", Toast.LENGTH_SHORT).show();
        });
        option3.setOnClickListener(view -> {
            Main.roboid = 3;
            Toast.makeText(this, "Robo Changed", Toast.LENGTH_SHORT).show();
        });
        option4.setOnClickListener(view -> {
            Main.roboid = 4;
            Toast.makeText(this, "Robo Changed", Toast.LENGTH_SHORT).show();
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