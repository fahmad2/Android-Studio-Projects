package com.example.urfi.know_your_government;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
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
 * Created by urfi on 4/17/17.
 */

public class OfficialActivity extends AppCompatActivity {

    String fbid;

    String ttid;

    String gpid;

    String ytid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official);

        Intent i = getIntent();

        final Official official = (Official) i.getSerializableExtra("Official");

        TextView tv = (TextView) findViewById(R.id.officiallLocation);
        tv.setText(i.getExtras().getString("loc",""));

        View view = this.getWindow().getDecorView();
        Official.PoliticalParty party = official.getParty();

        if(party.equals(Official.PoliticalParty.DEMOCRATIC)){
            view.setBackgroundColor(Color.BLUE);
            TextView tx = (TextView) findViewById(R.id.officialParty);
            tx.setText("("+party+")");
        }
        else if(party.equals(Official.PoliticalParty.REPUBLICAN)){
            view.setBackgroundColor(Color.RED);
            TextView tx = (TextView) findViewById(R.id.officialParty);
            tx.setText("("+party+")");
        }

        TextView office = (TextView) findViewById(R.id.photoOffice);
        office.setText(official.getOffice());

        TextView name = (TextView) findViewById(R.id.photoName);
        name.setText(official.getName());

        if(official.getAddress() != null) {
            TextView address = (TextView) findViewById(R.id.textView4);
            address.setText(official.getAddress());
        }

        if(official.getPhone() != null) {
            TextView phone = (TextView) findViewById(R.id.textView10);
            phone.setText(official.getPhone());
        }

        if(official.getEmail() != null) {
            TextView email = (TextView) findViewById(R.id.textView13);
            email.setText(official.getEmail());
        }

        if(official.getWebsite() != null){
            TextView website = (TextView) findViewById(R.id.textView15);
            website.setText(official.getWebsite());
        }

        loadImage(official.getPhoto());

        fbid = official.getFaceb();
        gpid = official.getgPlus();
        ttid = official.getTwit();
        ytid = official.getYout();


        ImageView officialImageView = (ImageView) findViewById(R.id.officialImage);
        officialImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(OfficialActivity.this, PhotoDetailActivity.class);

                String loc = ((TextView) findViewById(R.id.officiallLocation)).getText().toString();

                i.putExtra("loc", loc);
                i.putExtra("Official", official);

                startActivity(i);
            }
        });

        ImageView fb = (ImageView) findViewById(R.id.facebook);
        if(fbid != null){
            fb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    facebookClicked(v);
                }
            });
        }
        else {
            fb.setVisibility(View.INVISIBLE);
        }

        ImageView tt = (ImageView) findViewById(R.id.twitter);
        if(ttid != null){
            tt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    twitterClicked(v);
                }
            });
        }
        else {
            tt.setVisibility(View.INVISIBLE);
        }


        ImageView gp = (ImageView) findViewById(R.id.googleplus);
        if(gpid != null){
            gp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    googlePlusClicked(v);
                }
            });
        }
        else {
            gp.setVisibility(View.INVISIBLE);
        }

        ImageView yt = (ImageView) findViewById(R.id.youtube);
        if(ytid != null){
            yt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    youTubeClicked(v);
                }
            });
        }
        else {
            yt.setVisibility(View.INVISIBLE);
        }
    }

    public void facebookClicked(View v) {
        String FACEBOOK_URL = "https://www.facebook.com/"+fbid;
        String urlToUse;
        PackageManager packageManager = getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
                urlToUse = "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            }
            else { //older versions of fb app
                urlToUse = "fb://page/"+fbid;
            }
        }
        catch (PackageManager.NameNotFoundException e) {
            urlToUse = FACEBOOK_URL; //normal web url
        }
        Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
        facebookIntent.setData(Uri.parse(urlToUse)); startActivity(facebookIntent);
    }

    public void twitterClicked(View v) {
        Intent intent = null;
        String name = ttid;
        try {
            // get the Twitter app if possible
            getPackageManager().getPackageInfo("com.twitter.android", 0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + name));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        catch (Exception e) { // no Twitter app, revert to browser
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/" + name));
        }
        startActivity(intent);
    }


    public void googlePlusClicked(View v) {
        String name = gpid;
        Intent intent = null;
        try {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setClassName("com.google.android.apps.plus",
                    "com.google.android.apps.plus.phone.UrlGatewayActivity");
            intent.putExtra("customAppUri", name);
            startActivity(intent);
        }
        catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://plus.google.com/" + name)));
        }
    }

    public void youTubeClicked(View v) {
        String name = ytid;
        Intent intent = null;
        try {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage("com.google.android.youtube");
            intent.setData(Uri.parse("https://www.youtube.com/" + name));
            startActivity(intent);
        }
        catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/" + name)));
        }
    }

    public void loadImage (final String photoUrl){
        final ImageView officialImageView = (ImageView) findViewById(R.id.officialImage);
        if (photoUrl != null) {
            Picasso picasso = new Picasso.Builder(this).listener(new Picasso.Listener() {
                @Override
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) { // Here we try https if the http image attempt failed
                    final String changedUrl = photoUrl.replace("http:", "https:");
                    picasso.load(changedUrl) .error(R.drawable.brokenimage).placeholder(R.drawable.placeholder).into(officialImageView);
                }
            }).build();

            picasso.load(photoUrl).error(R.drawable.brokenimage).placeholder(R.drawable.placeholder) .into(officialImageView);
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