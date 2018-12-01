package com.dysplace.dysplaceproto1.main.utilities;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;

import com.dysplace.dysplaceproto1.event_listeners.MyMediaSourceEventListener;
import com.dysplace.dysplaceproto1.pojos.lambda_event_generator.AssetData;
import com.dysplace.dysplaceproto1.pojos.lambda_event_generator.LatLng;
import com.dysplace.dysplaceproto1.utilities.AppUtils;
import com.dysplace.dysplaceproto1.utilities.AssetPathUtils;
import com.dysplace.dysplaceproto1.utilities.GpsUtils;
import com.dysplace.dysplaceproto1.utilities.VARIABLES;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MediaSourceEventListener;

import java.io.IOException;
import java.util.List;

public class MediaPlayerUtils {

    private static final String LOG_TAG = "ExoMob MediaPlayerUtils";

    public static void setMute(boolean mute, SimpleExoPlayer exoPlayer){
        Log.i(LOG_TAG,"Setting Mute to: "+mute);

        exoPlayer.setVolume(mute ? VARIABLES.muteVolume : VARIABLES.unMuteVolume);
    }

    public static boolean isMute(SimpleExoPlayer exoPlayer){
        Log.i(LOG_TAG, "Checking if exoplayer is mute");

        float volume = exoPlayer.getVolume();

        return volume == VARIABLES.muteVolume;
    }

    private static void addMediaSourceToPlayer(ConcatenatingMediaSource concatMediaSource,
                                               MediaSource mediaSource){
        if(mediaSource != null){
            Log.i(LOG_TAG, "Adding MediaSource to player");
            concatMediaSource.addMediaSource(mediaSource);
        }
        else{
            Log.i(LOG_TAG, "MediaSource is null. Can not add to player");
        }
    }

    public static void addAssetsToQueue(Activity activity, ConcatenatingMediaSource concatMediaSource,
                                        List<AssetData> listAssetData){

        final String methodName = "addAssetsToQueue";
        final String METHOD_TAG = LOG_TAG+"/"+methodName;


        for(AssetData assetData: listAssetData){
            MediaSource mediaSource = AssetPathUtils.getSDMediaSourceWithCode(
                    assetData.getAssetID());
            if(mediaSource != null){
                Handler handler = new Handler();
                Log.i(METHOD_TAG, "Adding: "+assetData.getAssetID()+" to queue");
                MediaSourceEventListener mediaSourceEventListener = new MyMediaSourceEventListener(assetData) {
                    @Override
                    public void onMediaPeriodCreated(int windowIndex, MediaSource.MediaPeriodId mediaPeriodId) {
                        Log.i(METHOD_TAG, "onMediaPeriodCreated with assetData: "+assetData.toString());
                    }

                    @Override
                    public void onMediaPeriodReleased(int windowIndex, MediaSource.MediaPeriodId mediaPeriodId) {
                        Log.i(METHOD_TAG, "onMediaPeriodReleased with assetData: "+assetData.toString());
                        LambdaUtils.invokeReportAssetPlayed(activity, this.getAssetData(),
                                this.getLatLng(), this.getDate());
                    }

                    @Override
                    public void onLoadStarted(int windowIndex, @Nullable MediaSource.MediaPeriodId mediaPeriodId, LoadEventInfo loadEventInfo, MediaLoadData mediaLoadData) {
                        Log.i(METHOD_TAG, "onLoadStarted with assetData: "+assetData.toString());
                    }

                    @Override
                    public void onLoadCompleted(int windowIndex, @Nullable MediaSource.MediaPeriodId mediaPeriodId, LoadEventInfo loadEventInfo, MediaLoadData mediaLoadData) {
                        Log.i(METHOD_TAG, "onLoadCompleted with assetData: "+assetData.toString());
                    }

                    @Override
                    public void onLoadCanceled(int windowIndex, @Nullable MediaSource.MediaPeriodId mediaPeriodId, LoadEventInfo loadEventInfo, MediaLoadData mediaLoadData) {
                        Log.i(METHOD_TAG, "onLoadCancelled with assetData: "+assetData.toString());
                    }

                    @Override
                    public void onLoadError(int windowIndex, @Nullable MediaSource.MediaPeriodId mediaPeriodId, LoadEventInfo loadEventInfo, MediaLoadData mediaLoadData, IOException error, boolean wasCanceled) {
                        Log.i(METHOD_TAG, "onLoadError with assetData: "+assetData.toString());
                    }

                    @Override
                    public void onReadingStarted(int windowIndex, MediaSource.MediaPeriodId mediaPeriodId) {
                        Log.i(METHOD_TAG, "onReadingStarted with assetData: "+assetData.toString());
                        this.setLatLng(GpsUtils.getCurrentLatLng());
                        this.setDate(AppUtils.getCurrentDateTime());
                    }

                    @Override
                    public void onUpstreamDiscarded(int windowIndex, MediaSource.MediaPeriodId mediaPeriodId, MediaLoadData mediaLoadData) {
                        Log.i(METHOD_TAG, "onUpstreamDiscarded with assetData: "+assetData.toString());
                    }

                    @Override
                    public void onDownstreamFormatChanged(int windowIndex, @Nullable MediaSource.MediaPeriodId mediaPeriodId, MediaLoadData mediaLoadData) {
                        Log.i(METHOD_TAG, "onDownstreamFormatChanged with assetData: "+assetData.toString());
                    }
                };
                mediaSource.addEventListener(handler, mediaSourceEventListener);
                addMediaSourceToPlayer(concatMediaSource, mediaSource);
            }
        }
    }
}
