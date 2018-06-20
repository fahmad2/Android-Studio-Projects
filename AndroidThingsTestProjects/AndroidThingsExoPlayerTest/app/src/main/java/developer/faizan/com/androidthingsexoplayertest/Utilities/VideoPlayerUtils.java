package developer.faizan.com.androidthingsexoplayertest.Utilities;

import com.google.android.exoplayer2.SimpleExoPlayer;

/**
 * Created by urfi on 3/21/18.
 */

public class VideoPlayerUtils {

    public static void setMute(boolean mute, SimpleExoPlayer exoPlayer){
        final float muteVolume = 0f;
        final float unMuteVolume = 1f;
        exoPlayer.setVolume(mute ? muteVolume : unMuteVolume);
    }
}
