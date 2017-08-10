package com.example.urfi.faizan_ahmad_assignment4;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static android.R.attr.password;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener{

    private List<Stock> stockList = new ArrayList<Stock>();
    private RecyclerView recyclerView;
    private LinearLayoutManager llm;
    private StockAdapter sAdapter;
    private final MainActivity x = this;

    private final String url = "http://www.marketwatch.com/investing/stock/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);

        sAdapter = new StockAdapter(stockList, this);
        recyclerView.setAdapter(sAdapter);

        llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(llm);

        SwipeRefreshLayout.OnRefreshListener rf = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                for(Stock s: stockList){
                    doFinAsync(s.getSymbol());
                }
            }
        };
    }

    // opens marketwatch page for specific stock
    @Override
    public void onClick(View v) {

        int pos = recyclerView.getChildLayoutPosition(v);
        Stock stk = stockList.get(pos);
        String symb = stk.getSymbol();
        String u = url+symb;

        if(!isConnected()){
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setTitle("No Network Connection");
            builder1.setMessage("Can not go to MarketWatch page without a connection.");
            builder1.setCancelable(true);

            AlertDialog alert11 = builder1.create();
            alert11.show();
            return;
        }

        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(u));
        startActivity(i);
    }

    // pops up dialog asking to delete an entry
    @Override
    public boolean onLongClick(View v){
        final int pos = recyclerView.getChildLayoutPosition(v);
        Stock stk = stockList.get(pos);

        DialogInterface.OnClickListener dip = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                stockList.remove(pos);
                sAdapter.notifyDataSetChanged();
                dialog.cancel();
            }
        };

        DialogInterface.OnClickListener din = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        };

        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle("Are you sure you want to delete this stock?");
        builder1.setPositiveButton("Yes", dip);
        builder1.setNegativeButton("No", din);
        builder1.setCancelable(true);

        AlertDialog alert11 = builder1.create();
        alert11.show();

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    // add new stock
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(x);
        alertDialog.setTitle("STOCK SELECTION");
        alertDialog.setMessage("Please enter a stock symbol:");

        final EditText input = new EditText(MainActivity.this);
        input.setGravity(Gravity.CENTER_HORIZONTAL);
        input.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);

        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        if(!isConnected()){
                            dialog.cancel();
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(x);
                            builder1.setTitle("No Network Connection");
                            builder1.setMessage("Can not add a stock without a connection.");
                            builder1.setCancelable(true);

                            AlertDialog alert11 = builder1.create();
                            alert11.show();
                            return;
                        }

                        boolean b = checkDuplicate(input.getText().toString());
                        if(b){
                            dialog.cancel();
                            return;
                        }
                        doNameAsync(input.getText().toString());
                        dialog.cancel();
                    }
                });

        alertDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();

        return true;
    }



    public void doNameAsync(String sym){

        AsyncCompanyDataLoader alt = new AsyncCompanyDataLoader(this);
        alt.execute(sym);
    }

    public void doFinAsync(String sym){
        AsyncFinancialDataLoader alt = new AsyncFinancialDataLoader(this);
        alt.execute(sym);
    }

    public boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo == null ? false : networkInfo.isConnected();
    }

    public void addStock(Stock stock){
        stockList.add(stock);
        Collections.sort(stockList, new StockCompare());
        sAdapter.notifyDataSetChanged();
    }

    public boolean containsName(String s){

        for(int i=0; i<stockList.size(); i++){
            Stock stk = stockList.get(i);
            if(stk.symbol.equals(s)){
                return true;
            }
        }

        return false;
    }

    public void updateNameData(final List<Map<String, String>> lm){
        if(lm.size() == 1){
            Map<String, String> mp = lm.get(0);
            boolean bol = checkDuplicate(mp.get("company_symbol"));

            if(bol == false){
                Stock stk = new Stock(mp.get("company_symbol"), mp.get("company_name"), " ", " ");
                addStock(stk);
                doFinAsync(mp.get("company_symbol"));
            }
        }
        else if(lm.size() == 0){
            AlertDialog.Builder builder1 = new AlertDialog.Builder(x);
            builder1.setTitle("Symbol Not Found");
            builder1.setMessage("Data for stock symbol");
            builder1.setCancelable(true);

            AlertDialog alert11 = builder1.create();
            alert11.show();
        }
        else if(lm.size() > 1){

            String [] arr = new String[lm.size()];

            for(int i=0; i<arr.length; i++){
                Map<String, String> mp = lm.get(i);
                arr[i] = mp.get("company_symbol");
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Make a selection");
            builder.setItems(arr, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Map<String, String> mp = lm.get(which);

                    boolean bol = checkDuplicate(mp.get("company_symbol"));

                    if(bol == false){
                        Stock stk = new Stock(mp.get("company_symbol"), mp.get("company_name"), " ", " ");
                        addStock(stk);
                        doFinAsync(mp.get("company_symbol"));
                    }
                    dialog.cancel();
                }
            });

            AlertDialog alert11 = builder.create();
            alert11.show();
        }
    }

    public void updateFinancialData(Map<String, String> mp){
        String sym = mp.get("symbol");
        for(Stock s: stockList){
            if(s.getSymbol().equals(sym)){
                s.setChange(mp.get("change")+" ("+mp.get("cPercent")+"%)");
                s.setPrice(mp.get("price"));
                break;
            }
        }
        sAdapter.notifyDataSetChanged();
    }

    public boolean checkDuplicate(String inputStr){
        if(containsName(inputStr)){
            AlertDialog.Builder builder1 = new AlertDialog.Builder(x);
            builder1.setTitle("Duplicate Stock");
            builder1.setMessage("The stock already exists");
            builder1.setCancelable(true);

            AlertDialog alert11 = builder1.create();
            alert11.show();
            return true;
        }
        return false;
    }

    public static class StockCompare implements Comparator<Stock> {

        @Override
        public int compare(Stock a, Stock b) {
            // write comparison logic here like below , it's just a sample
            return a.symbol.compareTo(b.symbol);
        }
    }
}