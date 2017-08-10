package com.example.urfi.faizan_ahmad_assignment3;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by urfi on 3/1/17.
 */

public class EditActivity extends AppCompatActivity{

    private boolean bol; // checks if the note was saved using the menu save icon. This skips the dialog
    private boolean bola; // checks if the note being saved is a new one or a pre-existing one
    private boolean saved;
    private Note note;
    EditText eta;
    EditText etb;
    MainActivity mainAct = new MainActivity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        bol = false;
        bola = false;

        eta = (EditText) findViewById(R.id.noteTitle);
        etb = (EditText) findViewById(R.id.noteDesc);

        Intent intent = getIntent();
        if(intent.hasExtra(Note.class.getName())){
            System.out.println("dsf");
            bola = true;
            note = (Note) intent.getSerializableExtra(Note.class.getName());

            eta.setText(note.getTitle());

            etb.setText(note.getDesc());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(note != null && !saved) {
            saveNote(note.getTitle(), note.getDesc());
        }

        mainAct.saveAllNotes();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.saveNote:
                bol = true;
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            boolean b = eta.getText().length() > 0 || etb.getText().length() > 0;
            if(!bol && b) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                builder1.setMessage("Do you want to save the note?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if(!saved) {
                                    saveNote(eta.getText().toString(), etb.getText().toString());
                                    saved = true;
                                }
                                dialog.cancel();
                                finish();
                                finishActivity(107);
                                Intent intent = new Intent(EditActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                finish();
                                finishActivity(107);
                                Intent intent = new Intent(EditActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
                return true;
            }
            else{
                if(b && !saved){
                    saveNote(eta.getText().toString(), etb.getText().toString());
                    saved = true;
                }

                finish();
                finishActivity(107);
                Intent intent = new Intent(EditActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }
        return false;
    }

    public boolean saveNote(String title, String desc){

        DateFormat df = new SimpleDateFormat("EEE MMM dd',' hh:mm a");
        Date dateobj = new Date();
        df.format(dateobj);

        if(bola){
            note.setDate(dateobj+"");
            note.setTitle(title);
            note.setDesc(desc);
            int pos = mainAct.getPos();
            System.out.println("EditActivity "+pos);
            mainAct.list.add(pos, note);
        }
        else{
            note = new Note(title, dateobj.toString(), desc);
            mainAct.list.add(note);
        }

        return true;
    }
}
