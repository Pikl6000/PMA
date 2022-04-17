package com.example.smellgood;

import static com.example.smellgood.Defaults.NO_CURSOR;
import static com.example.smellgood.Defaults.DISMISS_ACTION;
import static com.example.smellgood.Defaults.NO_COOKIE;
import static com.example.smellgood.Defaults.NO_CURSOR;
import static com.example.smellgood.Defaults.NO_FLAGS;
import static com.example.smellgood.Defaults.NO_SELECTION;
import static com.example.smellgood.Defaults.NO_SELECTION_ARGS;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smellgood.provider.NoteContentProvider;
import com.example.smellgood.provider.Provider;

public class ScoreBoard extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener{
    private static final int NOTES_LOADER_ID = 0;
    private static final int INSERT_NOTE_TOKEN = 0;
    private static final int DELETE_NOTE_TOKEN = 0;
    private GridView notesGridView;
    private SimpleCursorAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        setContentView(R.layout.scoreboard_layout);
        getLoaderManager().initLoader(NOTES_LOADER_ID, Bundle.EMPTY, this);
        notesGridView = (GridView) findViewById(R.id.notesGridView);
        notesGridView.setAdapter(initializeAdapter());
        notesGridView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, final long id) {
        Cursor selectedNoteCursor = (Cursor) parent.getItemAtPosition(position);
        int dN = selectedNoteCursor.getColumnIndex(Provider.Note.NICKNAME),
                dL = selectedNoteCursor.getColumnIndex(Provider.Note.SCORE);
        String noteN = selectedNoteCursor.getString(dN),noteL = selectedNoteCursor.getString(dL);
        new AlertDialog.Builder(this)
                .setTitle("Hráč a skóre")
                .setMessage("Hráč : "+noteN+"\nSkóre : "+noteL)
                .setNegativeButton("Zavrieť", DISMISS_ACTION)
                .show();
    }

    private ListAdapter initializeAdapter() {
        String[] from = {Provider.Note.NICKNAME,Provider.Note.SCORE};
        int[] to = { R.id.notesGridViewItem };
        this.adapter = new SimpleCursorAdapter(this, R.layout.note, NO_CURSOR,
                from, to, NO_FLAGS);
        return this.adapter;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        CursorLoader loader = new CursorLoader(this);
        loader.setUri(NoteContentProvider.CONTENT_URI);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        this.adapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        this.adapter.swapCursor(NO_CURSOR);
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
