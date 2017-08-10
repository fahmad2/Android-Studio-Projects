package com.example.urfi.know_your_government;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by urfi on 4/16/17.
 */

public class OfficialViewHolder extends RecyclerView.ViewHolder {

    protected TextView office;
    protected TextView name;

    public OfficialViewHolder(View v){
        super(v);
        office = (TextView) v.findViewById(R.id.office);
        name = (TextView) v.findViewById(R.id.name);
    }
}
