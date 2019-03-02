package com.assignment.rahulhosmani.knowyourgovernment;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class OfficialsListViewHolder extends RecyclerView.ViewHolder {
    public TextView officeName, name;

    public OfficialsListViewHolder(@NonNull View view) {
        super(view);
        this.officeName = view.findViewById(R.id.officeName);
        this.name = view.findViewById(R.id.name);
    }
}
