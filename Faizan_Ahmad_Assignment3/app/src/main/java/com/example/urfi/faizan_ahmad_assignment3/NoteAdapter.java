package com.example.urfi.faizan_ahmad_assignment3;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by urfi on 2/28/17.
 */

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private List<Note> nList = new ArrayList<Note>();
    private MainActivity mainAct;

    public NoteAdapter(List<Note> ls, MainActivity mn){
        this.nList = ls;
        this.mainAct = mn;
    }

    public List<Note> getList(){
        return this.nList;
    }

    @Override
    public int getItemCount(){
        return nList.size();
    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);

        itemView.setOnClickListener(mainAct);

        return new NoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NoteViewHolder contactViewHolder, int i) {
        Note nd = nList.get(i);
        contactViewHolder.vTitle.setText(nd.title);
        contactViewHolder.vDate.setText(nd.date);
        contactViewHolder.vDesc.setText(nd.desc);
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder {

        protected TextView vTitle;
        protected TextView vDate;
        protected TextView vDesc;

        public NoteViewHolder(View v) {
            super(v);
            vTitle = (TextView) v.findViewById(R.id.noteTitle);
            vDate = (TextView)  v.findViewById(R.id.noteDate);
            vDesc = (TextView)  v.findViewById(R.id.noteDesc);
        }
    }
}
