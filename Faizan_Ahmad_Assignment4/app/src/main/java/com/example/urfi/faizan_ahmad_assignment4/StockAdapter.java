package com.example.urfi.faizan_ahmad_assignment4;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by urfi on 3/19/17.
 */

public class StockAdapter extends RecyclerView.Adapter<StockViewHolder>{

    private List<Stock> stockList = new ArrayList<Stock>();
    private MainActivity mainActivity;

    public StockAdapter(List<Stock> sl, MainActivity ma){
        this.stockList = sl;
        this.mainActivity = ma;
    }

    public List<Stock> getStockList() { return this.stockList; }

    @Override
    public StockViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);

        itemView.setOnClickListener(mainActivity);
        itemView.setOnLongClickListener(mainActivity);

        return new StockViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(StockViewHolder holder, int position) {
        Stock stk = stockList.get(position);

        holder.vSym.setText(stk.getSymbol());
        holder.vName.setText(stk.getName());

        String ch = stk.getChange();

        char [] carr = ch.toCharArray();
        if(carr[0] == '+'){
            holder.vSym.setTextColor(Color.GREEN);
            holder.vName.setTextColor(Color.GREEN);
            holder.vChange.setTextColor(Color.GREEN);
            holder.vPrice.setTextColor(Color.GREEN);

            holder.vChange.setText("▲ "+stk.getChange());
        }
        else if(carr[0] == '-'){
            holder.vSym.setTextColor(Color.RED);
            holder.vName.setTextColor(Color.RED);
            holder.vChange.setTextColor(Color.RED);
            holder.vPrice.setTextColor(Color.RED);

            holder.vChange.setText("▼ "+stk.getChange());
        }

        holder.vPrice.setText(stk.getPrice());
    }

    @Override
    public int getItemCount() {
        return stockList.size();
    }
}
