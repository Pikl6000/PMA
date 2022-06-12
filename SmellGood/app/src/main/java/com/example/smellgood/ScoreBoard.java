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
    static String[] player;
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


//    public void onItemClick(AdapterView<?> parent, View view, int position, final long id) {
//        Cursor selectedNoteCursor = (Cursor) parent.getItemAtPosition(position);
//        int dN = selectedNoteCursor.getColumnIndex(Provider.Note.NICKNAME),
//                dL = selectedNoteCursor.getColumnIndex(Provider.Note.SCORE);
//        String noteN = selectedNoteCursor.getString(dN),noteL = selectedNoteCursor.getString(dL);
//        new AlertDialog.Builder(this)
//                .setTitle("Hráč a skóre")
//                .setMessage("Hráč : "+noteN+"\nSkóre : "+noteL)
//                .setNegativeButton("Zavrieť", DISMISS_ACTION)
//                .show();
//    }

    private void initializeAdapter() {
        ArrayList<String> a = new ArrayList<>();
        Query top10Query = data.getDatabaseReference().orderByChild("score").limitToLast(10);
        top10Query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                    String key = userSnapshot.getKey();
                    a.add(key);
                    counter++;
                }
                player = new String[counter];
                int c = 0;
                for (String b : a){
                    player[c] = a.get(c);
                    c++;
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        gridView = (GridView) findViewById(R.id.notesGridView);
                        gridView.setAdapter(new CustomAdapter(ScoreBoard.this, player));
                        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                                Toast.makeText(getApplicationContext(), ((TextView) v.findViewById(R.id.grid_item_label)).getText(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });

    }

//    private void a(List<String> names){
//        String[] from = names.toArray(new String[names.size()]);
//        int[] to = { R.id.notesGridViewItem };
//        this.adapter = new SimpleCursorAdapter(this, R.layout.note, NO_CURSOR, from, to, NO_FLAGS);
//        notesGridView.setAdapter(adapter);
//    }

//    @Override
//    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
//        CursorLoader loader = new CursorLoader(this);
//        loader.setUri(NoteContentProvider.CONTENT_URI);
//        return loader;
//    }
//
////    @Override
////    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
////        this.adapter.swapCursor(cursor);
////    }
//
//    @Override
//    public void onLoaderReset(Loader<Cursor> loader) {
//        this.adapter.swapCursor(NO_CURSOR);
//    }



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
