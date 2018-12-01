package com.dysplace.dysplaceproto1.pojos.lambda_event_generator.report_asset_played;

import com.dysplace.dysplaceproto1.pojos.lambda_event_generator.LatLng;
import com.dysplace.dysplaceproto1.pojos.lambda_event_generator.AssetData;

import java.util.Date;

// Report Asset Played Request

public class RapRequest {

    private AssetData assetData;
    private LatLng latLng;
    private String date;
    private String deviceID;

    public RapRequest(AssetData assetData, LatLng latLng, String date, String deviceID){
        this.setAssetData(assetData);
        this.setLatLng(latLng);
        this.setDate(date);
        this.setDeviceID(deviceID);
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

    public String getDeviceID() {
        return this.deviceID;
    }

    public void setAssetData(AssetData assetData) {
        this.assetData = assetData;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    @Override
    public String toString(){
        return "(RapRequest) "+this.assetData.toString()+", "+this.latLng.toString()+
                ", Date: "+this.date+", DeviceID: "+this.deviceID;
    }
}
