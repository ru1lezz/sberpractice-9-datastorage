package com.serzhan.datastorage.sqlite;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.serzhan.datastorage.sqlite.entity.Note;
import com.serzhan.datastorage.sqlite.database.DBManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddNoteActivity extends AppCompatActivity {

    private EditText mContentEditText;
    private Button mAddNoteButton;
    private Button mCancelButton;

    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        initViews();
        initListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        executorService = Executors.newSingleThreadExecutor();
    }

    @Override
    protected void onPause() {
        super.onPause();
        executorService.shutdown();
    }

    private void initViews() {
        mContentEditText = findViewById(R.id.note_add_edit_text);
        mAddNoteButton = findViewById(R.id.activity_add_note_button);
        mCancelButton = findViewById(R.id.cancel_button);
    }

    private void initListeners() {
        mAddNoteButton.setOnClickListener(view -> {
            addNote();
            setResult(RESULT_OK);
            finish();
        });
        mCancelButton.setOnClickListener((view) -> {
            setResult(Activity.RESULT_CANCELED);
            finish();
        });
    }

    private void addNote() {
        Note note = getNote();
        executorService.execute(new AddNoteTask(note));
    }

    private Note getNote() {
        Note note = new Note();
        note.setContent(mContentEditText.getText().toString());
        return note;
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, AddNoteActivity.class);
    }

    class AddNoteTask implements Runnable {

        private Note note;

        public AddNoteTask(Note note) {
            this.note = note;
        }

        @Override
        public void run() {
            try {
                DBManager.getInstance(AddNoteActivity.this).addNote(note);
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
}
