package com.serzhan.datastorage.sqlite;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class NotesDecoration extends RecyclerView.ItemDecoration {
    private final Context mContext;

    public NotesDecoration(Context mContext) {
        this.mContext = mContext.getApplicationContext();
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if(parent.getChildAdapterPosition(view) == 0) {
            outRect.top = (int) mContext.getResources().getDimension(R.dimen.list_item_offset);
        }
        outRect.bottom = (int) mContext.getResources().getDimension(R.dimen.list_item_offset);
    }
}
