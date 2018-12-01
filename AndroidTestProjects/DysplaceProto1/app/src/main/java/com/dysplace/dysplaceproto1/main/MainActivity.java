package com.dysplace.dysplaceproto1.main;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.dysplace.dysplaceproto1.R;
import com.dysplace.dysplaceproto1.interfaces.aws_mobile_client.AwsMobHubActivity;
import com.dysplace.dysplaceproto1.main.utilities.LambdaUtils;
import com.dysplace.dysplaceproto1.main.utilities.MediaPlayerUtils;
import com.dysplace.dysplaceproto1.main.utilities.S3Utils;
import com.dysplace.dysplaceproto1.pojos.lambda_event_generator.LatLng;
import com.dysplace.dysplaceproto1.utilities.AssetPathUtils;
import com.dysplace.dysplaceproto1.utilities.AwsCogUtils;
import com.dysplace.dysplaceproto1.utilities.ConfigUtils;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;

public class MainActivity extends AppCompatActivity implements AwsMobHubActivity {

    private static final String LOG_TAG = "MainActivity";

    private PlayerView playerView;
    private SimpleExoPlayer player;
    private ConcatenatingMediaSource concatenatingMediaSource;

    private Button s3SyncBtn;
    private Button invokeGetIDsLambda;

    //Todo: Create onUnMute Custom Event Listener

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playerView = findViewById(R.id.mainExoPlayerView);

        s3SyncBtn = findViewById(R.id.bucketSyncBtn);

        invokeGetIDsLambda = findViewById(R.id.invokeGetIDs);

        ConfigUtils.configureActivity(this);

        AwsCogUtils.addSignInStateChangelistener(this);
    }

    @Override
    protected void onResume() {

        final String methodName = "onResume";
        final String METHOD_TAG = LOG_TAG+"/"+methodName;

        Log.i(METHOD_TAG, "Entered");

        super.onResume();

        initializeAwsMobileClient(this);
    }

    @Override
    protected void onPause() {

        final String methodName = "onPause";
        final String METHOD_TAG = LOG_TAG+"/"+methodName;

        Log.i(METHOD_TAG, "Entered");
        super.onPause();
        player.release();
        playerView.getOverlayFrameLayout().removeAllViews();

        s3SyncBtn.setVisibility(View.INVISIBLE);
        invokeGetIDsLambda.setVisibility(View.INVISIBLE);
    }

    private void initializePlayer(){

        final String methodName = "initializePlayer";
        final String METHOD_TAG = LOG_TAG+"/"+methodName;

        Log.d(METHOD_TAG, "Initializing Player");

        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector =
                new DefaultTrackSelector(videoTrackSelectionFactory);

        player = ExoPlayerFactory.newSimpleInstance(this, trackSelector);

        MediaPlayerUtils.setMute(true, player);

        MediaSource defaultMediaSource = AssetPathUtils.getDefaultMediaSource(this);

        if(defaultMediaSource == null){
            Log.i(METHOD_TAG, "No Default Video Found. Returning");
            return;
        }

        concatenatingMediaSource = new ConcatenatingMediaSource(defaultMediaSource);

        player.addListener(new Player.DefaultEventListener() {
            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                super.onPlayerStateChanged(playWhenReady, playbackState);

                if(playbackState == Player.STATE_ENDED){
                    Log.i(METHOD_TAG,"Adding defaultMediaSource to the Queue");

                    concatenatingMediaSource = new ConcatenatingMediaSource(defaultMediaSource);

                    player.prepare(concatenatingMediaSource);
                }
            }
        });

        playerView.setPlayer(player);
        player.prepare(concatenatingMediaSource);
        player.setPlayWhenReady(true);
    }

    @Override
    public void onConnectToAWS() {
        initializePlayer();

        Activity activity = this;

        s3SyncBtn.setVisibility(View.VISIBLE);
        invokeGetIDsLambda.setVisibility(View.VISIBLE);

        s3SyncBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                S3Utils.syncWithBucket(activity);
            }
        });

        invokeGetIDsLambda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LambdaUtils.invokeGetNewAssetIDs(activity, new LatLng("", ""),
                        concatenatingMediaSource);
            }
        });

    }
}
