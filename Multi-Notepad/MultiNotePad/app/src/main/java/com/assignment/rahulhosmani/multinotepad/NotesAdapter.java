package com.assignment.rahulhosmani.multinotepad;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesViewHolder> {

    private List<Notes> notesList;
    private MainActivity mainAct;
    private static final String TAG = "NotesAdapter";

    public NotesAdapter(List<Notes> notesList, MainActivity mainActivity) {
        this.notesList = notesList;
        this.mainAct = mainActivity;

    }

    @Override
    public NotesViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: Making New View Holder");
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_element, parent,false);

       itemView.setOnClickListener(mainAct);
       itemView.setOnLongClickListener(mainAct);

        return new NotesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder notesViewHolder, int position) {
        Log.d(TAG, "onBindViewHolder: Filling View Holder");
        Notes note = notesList.get(position);
        notesViewHolder.title.setText(note.getTitle());
        notesViewHolder.date.setText(note.getDate());
        notesViewHolder.content.setText(note.getContent());
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }
}
