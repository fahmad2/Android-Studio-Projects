package com.example.urfi.faizan_ahmad_assignment3;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.JsonWriter;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static List<Note> list = new ArrayList<Note>();
    private RecyclerView recyclerView;
    private LinearLayoutManager llm;
    private NoteAdapter mAdapter;
    private int posi;
    private FileOutputStream fos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);

        mAdapter = new NoteAdapter(list, this);
        recyclerView.setAdapter(mAdapter);

        llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(llm);

        if(list.size() > 0){
            new AsyncNoteLoader(this).execute();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        saveAllNotes();
    }

    @Override
    public void onClick(View v) {
        int pos = recyclerView.getChildLayoutPosition(v);
        setPos(pos);
        Note nt = list.get(pos);
        Intent intent = new Intent(MainActivity.this, EditActivity.class);
        intent.putExtra(Note.class.getName(), nt);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.about:
                Intent intent1 = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent1);
                break;
            case R.id.newNote:
                Intent intent2 = new Intent(MainActivity.this, EditActivity.class);
                startActivity(intent2);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    public List<Note> getList(){
        return this.list;
    }

    public int getPos(){
        return this.posi;
    }

    public void setPos(int p){
        this.posi = p;
    }

    protected void saveAllNotes(){
        try {
            fos = getApplicationContext().openFileOutput("Note.json", Context.MODE_PRIVATE);

            JsonWriter writer = new JsonWriter(new OutputStreamWriter(fos, "UTF-8"));

            for(Note nt: list){
                writer.setIndent("  ");
                writer.beginObject();
                writer.name("title").value(nt.getTitle());
                writer.name("description").value(nt.getDesc());
                writer.name("date").value(nt.getDate());
                writer.endObject();
            }
            writer.close();

            //Toast.makeText(this, "Note Saved", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e) {
            e.getStackTrace();
        }
    }

    public void updateData(List<Note> list){
        this.list.addAll(list);
        mAdapter.notifyDataSetChanged();
    }
}