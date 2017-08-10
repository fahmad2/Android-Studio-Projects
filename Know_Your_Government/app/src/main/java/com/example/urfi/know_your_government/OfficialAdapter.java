package com.example.urfi.know_your_government;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by urfi on 4/16/17.
 */

public class OfficialAdapter extends RecyclerView.Adapter<OfficialViewHolder> {

    private List<Official> officialList = new ArrayList<Official>();
    private MainActivity mainActivity;

    public OfficialAdapter (List<Official> list, MainActivity ma){
        this.officialList = list;
        this.mainActivity = ma;
    }

    public List<Official> getOfficialList() { return this.officialList; }

    @Override
    public OfficialViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);

        itemView.setOnClickListener(mainActivity);
        itemView.setOnLongClickListener(mainActivity);

        return new OfficialViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(OfficialViewHolder holder, int position) {

        Official official = officialList.get(position);

        holder.office.setText(official.getOffice());
        holder.name.setText(official.getName()+" ("+official.getParty()+")");

    }

    @Override
    public int getItemCount() {
        return officialList.size();
    }
}