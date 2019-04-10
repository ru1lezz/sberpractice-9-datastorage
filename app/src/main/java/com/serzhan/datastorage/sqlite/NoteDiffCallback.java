package com.serzhan.datastorage.sqlite;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import com.serzhan.datastorage.sqlite.entity.Note;

import java.util.List;

public class NoteDiffCallback extends DiffUtil.Callback {

    private final List<Note> mOldList;
    private final List<Note> mNewList;

    public NoteDiffCallback(List<Note> mOldList, List<Note> mNewList) {
        this.mOldList = mOldList;
        this.mNewList = mNewList;
    }

    @Override
    public int getOldListSize() {
        return mOldList.size();
    }

    @Override
    public int getNewListSize() {
        return mNewList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        Note oldNote = mOldList.get(oldItemPosition);
        Note newNote = mNewList.get(newItemPosition);

        return oldNote.getId() == newNote.getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Note oldNote = mOldList.get(oldItemPosition);
        Note newNote = mNewList.get(newItemPosition);

        return oldNote.getContent().equals(newNote.getContent());
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        Note oldNote = mOldList.get(oldItemPosition);
        Note newNote = mNewList.get(newItemPosition);
        Bundle diff = new Bundle();
        if(!(oldNote.getContent().equals(newNote.getContent()))) {
            diff.putString("content", newNote.getContent());
        }
        if(diff.size() == 0) {
            return null;
        }
        return diff;
    }
}
