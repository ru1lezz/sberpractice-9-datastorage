package com.serzhan.datastorage.sqlite;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import com.serzhan.datastorage.sqlite.entity.Note;
import com.serzhan.datastorage.sqlite.database.DBManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NoteActivity extends AppCompatActivity {
    private static final String EXTRA_NOTE_ID = "extra_note_id";

    private Note mNote;
    private ExecutorService executorService;

    private EditText mContentEditText;

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1: {
                    mNote = (Note)msg.obj;
                    mContentEditText.setText(mNote.getContent());
                    break;
                }
                default:
                    super.handleMessage(msg);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        mContentEditText = findViewById(R.id.edit_note_edit_text);
        executorService = Executors.newSingleThreadExecutor();
        initNote();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initNote() {
        executorService.execute(new GetNoteTask(mHandler));
    }

    private void updateNote() {
        mNote.setContent(mContentEditText.getText().toString());
        executorService.execute(new UpdateNoteTask(mNote));
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.acitvity_note_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.activity_note_menu_apply_item: {
                updateNote();
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    class GetNoteTask implements Runnable {

        private Handler mHandler;

        public GetNoteTask(Handler mHandler) {
            this.mHandler = mHandler;
        }

        @Override
        public void run() {
            int id = getIntent().getExtras().getInt(EXTRA_NOTE_ID);
            Note note = DBManager.getInstance(NoteActivity.this).getNoteById(id);
            Message msg = mHandler.obtainMessage(1, note);
            msg.sendToTarget();
        }
    }

    class UpdateNoteTask implements Runnable {
        private Note mNote;

        public UpdateNoteTask(Note note) {
            this.mNote = note;
        }

        @Override
        public void run() {
            DBManager.getInstance(NoteActivity.this).updateNote(mNote);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static Intent newIntent(Context context, int id) {
        Intent intent = new Intent(context, NoteActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_NOTE_ID, id);
        intent.putExtras(bundle);
        return intent;
    }
}
