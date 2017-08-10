package com.example.urfi.know_your_government;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by urfi on 4/20/17.
 */

public class PhotoDetailActivity extends AppCompatActivity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);

        Intent i = getIntent();

        final Official official = (Official) i.getSerializableExtra("Official");

        TextView tv = (TextView) findViewById(R.id.officiallLocation);
        tv.setText(i.getExtras().getString("loc",""));

        View view = this.getWindow().getDecorView();
        Official.PoliticalParty party = official.getParty();

        if(party.equals(Official.PoliticalParty.DEMOCRATIC)){
            view.setBackgroundColor(Color.BLUE);
            TextView tx = (TextView) findViewById(R.id.photoOffice);
            tx.setText(official.getOffice());
        }
        else if(party.equals(Official.PoliticalParty.REPUBLICAN)){
            view.setBackgroundColor(Color.RED);
            TextView tx = (TextView) findViewById(R.id.photoOffice);
            tx.setText(official.getOffice());
        }

        TextView name = (TextView) findViewById(R.id.photoName);
        name.setText(official.getName());

        loadImage(official.getPhoto());
    }

    public void loadImage (final String photoUrl){
        final ImageView officialImageView = (ImageView) findViewById(R.id.photoImageView);
        if (photoUrl != null) {
            Picasso picasso = new Picasso.Builder(this).listener(new Picasso.Listener() {
                @Override
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) { // Here we try https if the http image attempt failed
                    final String changedUrl = photoUrl.replace("http:", "https:");
                    picasso.load(changedUrl) .error(R.drawable.brokenimage).placeholder(R.drawable.placeholder).into(officialImageView);
                }
            }).build();

            picasso.load(photoUrl).error(R.drawable.brokenimage).placeholder(R.drawable.placeholder).into(officialImageView);
        }
        else {
            Picasso.with(this).load(photoUrl).error(R.drawable.brokenimage).placeholder(R.drawable.missing).into(officialImageView);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            finishActivity(107);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            return true;
        }
        return false;
    }
}
