package com.example.urfi.faizan_ahmad_assignment4;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by urfi on 3/19/17.
 */

public class StockViewHolder extends RecyclerView.ViewHolder {

    protected TextView vSym;
    protected TextView vPrice;
    protected TextView vChange;
    protected TextView vName;

    public StockViewHolder(View v) {
        super(v);
        vSym = (TextView) v.findViewById(R.id.stkSym);
        vPrice = (TextView) v.findViewById(R.id.stkPrice);
        vChange = (TextView) v.findViewById(R.id.stkChange);
        vName = (TextView) v.findViewById(R.id.stkName);
    }
}
