package com.example.smellgood.provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.smellgood.Defaults;
import static android.provider.BaseColumns._ID;
import static com.example.smellgood.provider.Provider.Note.SCORE;
import static com.example.smellgood.provider.Provider.Note.NICKNAME;
import static com.example.smellgood.provider.Provider.Note.TABLE_NAME;
import static com.example.smellgood.Defaults.DEFAULT_CURSOR_FACTORY;

public class DatabaseOpenHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "film";
    public static final int DATABASE_VERSION = 1;
    public DatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, DEFAULT_CURSOR_FACTORY, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createTableSql());
        insertSampleEntry(db, "SCOREBOARD","SCORE");
        insertSampleEntry(db, "Mirek","-1");
    }
    private void insertSampleEntry(SQLiteDatabase db, String name,String score) {
        ContentValues contentValues = new ContentValues();
        System.out.println(name);
        System.out.println(score);
        contentValues.put(NICKNAME, name);
        contentValues.put(SCORE, score);
        db.insert(Provider.Note.TABLE_NAME, Defaults.NO_NULL_COLUMN_HACK, contentValues);
        System.out.println("Added");
    }
    private String createTableSql() {
        String sqlTemplate = "CREATE TABLE %s ("
                + "%s INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "%s NICKNAME,"
                + "%s SCORE"
                + ")";
        return String.format(sqlTemplate, TABLE_NAME, _ID, NICKNAME, SCORE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // do nothing
    }
}
