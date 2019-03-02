package com.assignment.rahulhosmani.multinotepad;

import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.TextView;

public class NotesViewHolder extends ViewHolder {

    public TextView title;
    public TextView date;
    public TextView content;

    public NotesViewHolder(View itemView) {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.textViewTitle);
        date = (TextView) itemView.findViewById(R.id.textViewDate);
        content = (TextView) itemView.findViewById(R.id.textViewContent);
    }
}
