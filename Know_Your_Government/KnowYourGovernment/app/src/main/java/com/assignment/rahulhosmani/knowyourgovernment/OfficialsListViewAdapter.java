package com.assignment.rahulhosmani.knowyourgovernment;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class OfficialsListViewAdapter extends RecyclerView.Adapter<OfficialsListViewHolder> {
    private List<Official> officialsObjList;
    private MainActivity mainActivity;

    public OfficialsListViewAdapter(List<Official> officialsObjList, MainActivity mainActivity) {
        this.officialsObjList = officialsObjList;
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public OfficialsListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_of_political_official_view, parent,false);

        view.setOnClickListener(mainActivity);
        return new OfficialsListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OfficialsListViewHolder viewHolder, int index) {
        viewHolder.officeName.setText(officialsObjList.get(index).getOfficeName());
        viewHolder.name.setText(officialsObjList.get(index).getName() + " ("+ officialsObjList.get(index).getPartyName() + ")");
    }

    @Override
    public int getItemCount() {
        return officialsObjList.size();
    }
}
