package com.serzhan.datastorage.sqlite;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.serzhan.datastorage.sqlite.entity.Note;
import com.serzhan.datastorage.sqlite.database.DBManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private static final int SET_NOTES = 1;
    private static final int NOTE_SETTINGS = 2;

    private RecyclerView mRecyclerView;
    private NoteAdapter mNoteAdapter;
    private FloatingActionButton mAddButton;
    private ExecutorService executor;

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SET_NOTES: {
                    mNoteAdapter.setNotes((ArrayList<Note>)msg.obj);
                }
                default:
                    super.handleMessage(msg);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initListeners();
        initRecyclerView();
        executor = Executors.newSingleThreadExecutor();
    }

    @Override
    protected void onResume() {
        super.onResume();
        executor.execute(new GetNotesTask(mHandler));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.activity_settings_menu_settings: {
                startActivityForResult(SettingsActivity.newIntent(MainActivity.this), NOTE_SETTINGS);
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initViews() {
        mAddButton = findViewById(R.id.add_note_button);
        mRecyclerView = findViewById(R.id.note_recycler_view);
    }

    private void initListeners() {
        mAddButton.setOnClickListener((view) -> startActivity(AddNoteActivity.newIntent(MainActivity.this)));
    }

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, RecyclerView.VERTICAL, false));
        mRecyclerView.addItemDecoration(new NotesDecoration(MainActivity.this));
        mNoteAdapter = new NoteAdapter(MainActivity.this);
        mRecyclerView.setAdapter(mNoteAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case NOTE_SETTINGS:
                if(resultCode == Activity.RESULT_OK) {
                    mNoteAdapter.updateUi();
                }
        }
    }

    class GetNotesTask implements Runnable {

        private Handler mHandler;

        GetNotesTask(Handler mHandler) {
            this.mHandler = mHandler;
        }

        @Override
        public void run() {
            try {
                List<Note> notes = DBManager.getInstance(MainActivity.this).getNotes();
                Message message = mHandler.obtainMessage(SET_NOTES, notes);
                message.sendToTarget();
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
}
