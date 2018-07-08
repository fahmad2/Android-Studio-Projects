package com.faizan.exoplayer2mobile.main;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.faizan.exoplayer2mobile.R;
import com.faizan.exoplayer2mobile.utils.AppUtils;
import com.faizan.exoplayer2mobile.utils.AssetPathUtils;
import com.faizan.exoplayer2mobile.utils.MediaPlayerUtils;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.LoopingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MediaSourceEventListener;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends Activity {

    private static final String TAG = "ExoMob MainActivity";
    private PlayerView playerView;
    private SimpleExoPlayer player;
    private ConcatenatingMediaSource concatenatingMediaSource;
    private MediaSource[] mediaSourcesArray;
    private LoopingMediaSource loopingMediaSource;

    List<String> videoPathList;
    List<MediaSource> mediaSources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "Entered onCreate");
        super.onCreate(savedInstanceState);
        AppUtils.disableNotificationBar(this);
        setContentView(R.layout.activity_main);

        Button addVideosButton = findViewById(R.id.addVideosButton);
        Button muteButton = findViewById(R.id.muteButton);
        playerView = findViewById(R.id.exo_player_view);

        addVideosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addVideos();
            }
        });

        muteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isMute = MediaPlayerUtils.isMute(player);
                if(isMute){
                    Log.i(TAG, "Video is muted");
                    Log.i(TAG, "Amplifying video");
                    MediaPlayerUtils.setMute(false, player);
                }
                else{
                    Log.i(TAG, "Video is not muted");
                    Log.i(TAG, "Muting video");
                    MediaPlayerUtils.setMute(true, player);
                }
            }
        });


    }

    @Override
    protected void onResume() {
        Log.i(TAG, "Entered onResume");
        super.onResume();
        this.initializePlayer();
    }

    @Override
    protected void onPause() {
        Log.i(TAG, "Entered onPause");
        super.onPause();
        player.release();
        playerView.getOverlayFrameLayout().removeAllViews();
    }

    private void initializePlayer(){
        Log.d(TAG, "Initializing Player");

        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector =
                new DefaultTrackSelector(videoTrackSelectionFactory);

        player = ExoPlayerFactory.newSimpleInstance(this, trackSelector);

        mediaSourcesArray =
                AssetPathUtils.getMediaSources(getApplicationContext());

        if(mediaSourcesArray.length == 0){
            Log.i(TAG, "No Asset Found. Returning");
            return;
        }

        MediaSource defaultMediaSource = mediaSourcesArray[0];

        concatenatingMediaSource =
                new ConcatenatingMediaSource(defaultMediaSource);

        loopingMediaSource = new LoopingMediaSource(concatenatingMediaSource);

        playerView.setPlayer(player);
        player.prepare(loopingMediaSource);
        player.setPlayWhenReady(true);
    }

    public void addVideos(){
        Log.i(TAG, "addVideosButton clicked");

        //Set<MediaSource> remainingMediaSources = new HashSet<>(Arrays.asList(mediaSourcesArray).subList(1, mediaSourcesArray.length));
        Set<MediaSource> remainingMediaSources = new HashSet<>();

        for(int i=1; i<mediaSourcesArray.length; i++){
            MediaSource mediaSource = mediaSourcesArray[i];
        }

        concatenatingMediaSource.addMediaSources(remainingMediaSources);
    }
}
