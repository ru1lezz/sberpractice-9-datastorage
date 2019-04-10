package com.serzhan.datastorage.sqlite.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final int VERSION_DB = 1;
    private static final String DB_NAME = "note_database";
    static final String TABLE_NAME = "notes";
    static final String NOTE_ID = "note_id";
    static final String NOTE_CONTENT = "content";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, VERSION_DB);
    }

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory cursorFactory, int version) {
        super(context, name, cursorFactory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createEmptyTables(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        deleteTables(db);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }

    private void createEmptyTables(SQLiteDatabase database) {
        database.execSQL(String.format("CREATE TABLE %s(%s integer primary key autoincrement, %s text)",
                TABLE_NAME, NOTE_ID, NOTE_CONTENT));
    }

    private void deleteTables(SQLiteDatabase db) {
        getReadableDatabase().execSQL(String.format("DROP TABLE IF EXISTS %s", TABLE_NAME));
    }
}
