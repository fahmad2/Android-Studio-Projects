package com.dysplace.dysplaceproto1.pojos.lambda_event_generator;

public class AssetData {

    private String assetID;
    private String campID;

    public AssetData(String assetID, String campID){
        this.setAssetID(assetID);
        this.setCampID(campID);
    }

    public String getAssetID() {
        return this.assetID;
    }

    public String getCampID() {
        return this.campID;
    }

    public void setAssetID(String assetID) {
        this.assetID = assetID;
    }

    public void setCampID(String campID) {
        this.campID = campID;
    }

    @Override
    public String toString(){
        return "AssetID: "+assetID+", CampaignID: "+campID;
    }
}
