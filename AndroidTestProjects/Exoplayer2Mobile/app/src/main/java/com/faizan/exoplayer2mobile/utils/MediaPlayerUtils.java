package com.faizan.exoplayer2mobile.utils;

import android.util.Log;

import com.google.android.exoplayer2.SimpleExoPlayer;

public class MediaPlayerUtils {

    private static final String TAG = "ExoMob MediaPlayerUtils";
    private static final float muteVolume = 0f;
    private static final float unMuteVolume = 1f;

    public static void setMute(boolean mute, SimpleExoPlayer exoPlayer){
        Log.i(TAG,"Setting Mute to: "+mute);

        exoPlayer.setVolume(mute ? muteVolume : unMuteVolume);
    }

    public static boolean isMute(SimpleExoPlayer exoPlayer){
        Log.i(TAG, "Checking if exoplayer is mute");

        float volume = exoPlayer.getVolume();

        return volume == muteVolume;
    }
}
