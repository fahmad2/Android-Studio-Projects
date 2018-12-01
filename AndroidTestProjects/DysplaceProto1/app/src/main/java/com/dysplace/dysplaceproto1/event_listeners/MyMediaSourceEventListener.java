package com.dysplace.dysplaceproto1.event_listeners;

import com.dysplace.dysplaceproto1.pojos.lambda_event_generator.AssetData;
import com.dysplace.dysplaceproto1.pojos.lambda_event_generator.LatLng;
import com.google.android.exoplayer2.source.MediaSourceEventListener;

import java.util.Date;

public abstract class MyMediaSourceEventListener implements MediaSourceEventListener {

    private AssetData assetData;
    private LatLng latLng;
    private String date;

    public MyMediaSourceEventListener(AssetData assetData){
        this.setAssetData(assetData);
    }

    public AssetData getAssetData() {
        return this.assetData;
    }

    public LatLng getLatLng() {
        return this.latLng;
    }

    public String getDate() {
        return this.date;
    }

    public void setAssetData(AssetData assetData) {
        this.assetData = assetData;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public void setDate(String date){
        this.date = date;
    }
}
