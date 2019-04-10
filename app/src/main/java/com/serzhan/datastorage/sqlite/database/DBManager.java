package com.serzhan.datastorage.sqlite.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.serzhan.datastorage.sqlite.entity.Note;

import java.util.ArrayList;
import java.util.List;

public class DBManager {

    private static DBManager sDbManager;

    private DBHelper dbHelper;

    public DBManager(Context context) {
        this.dbHelper = new DBHelper(context);
    }

    public void addNote(Note note) {
        SQLiteDatabase db = null;
        try {
            db = dbHelper.getWritableDatabase();
            ContentValues contentValues = getContentValues(note);
            db.insert(DBHelper.TABLE_NAME, null, contentValues);
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            if(db != null) {
                if(db.inTransaction()) {
                    db.endTransaction();
                }
                db.close();
            }
        }
    }

    public void updateNote(Note note) {
        SQLiteDatabase db = null;
        try {
            db = dbHelper.getWritableDatabase();
            ContentValues contentValues = getContentValues(note);
            db.update(DBHelper.TABLE_NAME, contentValues, DBHelper.NOTE_ID + "=?", new String[]{String.valueOf(note.getId())});
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            if(db != null) {
                if(db.inTransaction()) {
                    db.endTransaction();
                }
                db.close();
            }
        }
    }

    public List<Note> getNotes() {
        List<Note> notes = null;
        SQLiteDatabase db = null;
        try {
            db = dbHelper.getReadableDatabase();
            db.beginTransaction();
            Cursor cursor = db.rawQuery(String.format("SELECT * FROM %s", DBHelper.TABLE_NAME), null);
            notes = parseNotes(cursor);
            cursor.close();
            db.setTransactionSuccessful();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            if(db != null) {
                if(db.inTransaction()) {
                    db.endTransaction();
                }
                db.close();
            }
        }
        return notes;
    }

    public Note getNoteById(int id) {
        Note note = null;
        SQLiteDatabase db = null;
        try {
            db = dbHelper.getReadableDatabase();
            db.beginTransaction();
            Cursor cursor = db.rawQuery(String.format("SELECT * FROM %s WHERE note_id = \"%d\"", DBHelper.TABLE_NAME, id),  null);
            note = parseNote(cursor);
            cursor.close();
            db.setTransactionSuccessful();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            if(db != null) {
                if(db.inTransaction()) {
                    db.endTransaction();
                }
                db.close();
            }
        }
        return note;
    }

    private ContentValues getContentValues(Note note) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.NOTE_CONTENT, note.getContent());

        return contentValues;
    }

    private List<Note> parseNotes(Cursor cursor) {
        List<Note> notes = new ArrayList<>();
        if(cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                Note note = getNote(cursor);
                notes.add(note);
                cursor.moveToNext();
            }
        }
        return notes;
    }

    private Note parseNote(Cursor cursor) {
        Note note = null;
        if(cursor.moveToFirst()) {
            note = getNote(cursor);
        }
        return note;
    }

    private Note getNote(Cursor cursor) {
        Note note = new Note();
        note.setId(cursor.getInt(cursor.getColumnIndex(DBHelper.NOTE_ID)));
        note.setContent(cursor.getString(cursor.getColumnIndex(DBHelper.NOTE_CONTENT)));

        return note;
    }

    public static DBManager getInstance(Context context) {
        if(sDbManager == null) {
            sDbManager = new DBManager(context);
        }

        return sDbManager;
    }
}
