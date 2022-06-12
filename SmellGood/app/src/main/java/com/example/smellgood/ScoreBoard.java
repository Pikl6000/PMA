package com.example.smellgood;


import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ScoreBoard extends AppCompatActivity {
    private static final int NOTES_LOADER_ID = 0;
    private static final int INSERT_NOTE_TOKEN = 0;
    private static final int DELETE_NOTE_TOKEN = 0;
    private SimpleCursorAdapter adapter;
    Fdata data;
    private FirebaseAuth mAuth;
    GridView gridView;
    static final String[] MOBILE_OS = new String[] {
            "Mirko", "Mejo","Pikl", "100","Pikl","Pikl","Pikl","Pikl","Pikl","Pikl"};
    static String[][] player;
    static int counter = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        setContentView(R.layout.scoreboard_layout);

        checkInternet();
        mAuth = FirebaseAuth.getInstance();
        data = Main.data;
        initializeAdapter();
    }

    public void checkInternet(){
        if (!isNetworkAvailable()){
            startActivity(new Intent(ScoreBoard.this, NoInternet.class));
        }
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void initializeAdapter() {
        Query top10Query = data.getDatabaseReference().orderByChild("score").limitToLast(10);
        top10Query.addValueEventListener(new ValueEventListener() {
            ArrayList<String> a = new ArrayList<>();
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                counter = 0;
                for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                    Player z = userSnapshot.getValue(Player.class);
                    String key = z.getNickname();
                    String scor = z.getScore();
                    System.out.println(key);
                    System.out.println(scor);
                    a.add(key);
                    a.add(scor);
                    counter++;
                }
                System.out.println("a = "+a.size());
                System.out.println(counter);
                player = new String[counter][2];
                Collections.reverse(a);
                System.out.println("R : "+a.size()/2);
                int c = 0;
                for (int i = 0; i < counter; i++) {
                    player[i][0] = a.get(c+1);
                    player[i][1] = a.get(c);
                    System.out.println("P : "+ player[i][0]+" "+  player[i][1]);
                    c = c+2;
                }
                System.out.println(Arrays.toString(player));
//                for (String b : a){
//                    player[c] = b;
//                    c++;
//                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        gridView = (GridView) findViewById(R.id.notesGridView);
                        gridView.setAdapter(null);
                        gridView.setAdapter(new CustomAdapter(ScoreBoard.this, player));
//                        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
//
//                            }
//                        });
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
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
