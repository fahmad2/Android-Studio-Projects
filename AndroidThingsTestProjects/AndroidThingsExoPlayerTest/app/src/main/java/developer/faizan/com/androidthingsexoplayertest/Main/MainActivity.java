package developer.faizan.com.androidthingsexoplayertest.Main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.LoopingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;

import java.util.ArrayList;
import java.util.List;

import developer.faizan.com.androidthingsexoplayertest.R;
import developer.faizan.com.androidthingsexoplayertest.tempManager.TemperatureService;
import developer.faizan.com.androidthingsexoplayertest.Utilities.VideoPathUtils;
import developer.faizan.com.androidthingsexoplayertest.Utilities.VideoPlayerUtils;

/**
 * Skeleton of an Android Things activity.
 * <p>
 * Android Things peripheral APIs are accessible through the class
 * PeripheralManagerService. For example, the snippet below will open a GPIO pin and
 * set it to HIGH:
 * <p>
 * <pre>{@code
 * PeripheralManagerService service = new PeripheralManagerService();
 * mLedGpio = service.openGpio("BCM6");
 * mLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
 * mLedGpio.setValue(true);
 * }</pre>
 * <p>
 * For more complex peripherals, look for an existing user-space driver, or implement one if none
 * is available.
 *
 * @see <a href="https://github.com/androidthings/contrib-drivers#readme">https://github.com/androidthings/contrib-drivers#readme</a>
 */
public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";

    PlayerView playerView;
    SimpleExoPlayer player;
    List<String> videoPathList;
    List<MediaSource> mediaSources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        videoPathList = new ArrayList<>();
        mediaSources = new ArrayList<>();

        playerView = findViewById(R.id.exo_player_view);

        startService(new Intent(this, TemperatureService.class));
        //startService(new Intent(this, GpsDriverService.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        initializePlayer();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "Entered onDestroy");
        super.onDestroy();
        player.release();
        playerView.getOverlayFrameLayout().removeAllViews();
    }

    private void initializePlayer(){
        boolean shouldMute = true;

        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector =
                new DefaultTrackSelector(videoTrackSelectionFactory);

        player =
                ExoPlayerFactory.newSimpleInstance(this, trackSelector);

        MediaSource [] mediaSourcesArray =
                VideoPathUtils.getMediaSources(getApplicationContext());

        ConcatenatingMediaSource concatenatingMediaSource =
                new ConcatenatingMediaSource(mediaSourcesArray);

        LoopingMediaSource loopingMediaSource = new LoopingMediaSource(concatenatingMediaSource);

        playerView.setPlayer(player);
        VideoPlayerUtils.setMute(shouldMute, player);
        player.prepare(loopingMediaSource);
        player.setPlayWhenReady(true);
    }
}
