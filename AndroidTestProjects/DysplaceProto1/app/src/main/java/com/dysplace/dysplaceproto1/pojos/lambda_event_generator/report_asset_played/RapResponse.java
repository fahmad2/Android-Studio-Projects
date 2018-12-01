package com.dysplace.dysplaceproto1.pojos.lambda_event_generator.report_asset_played;

// Report Asset Played Response

public class RapResponse {

    private int responseCode;

    public RapResponse(){}

    public RapResponse(int responseCode) {
        this.setResponseCode(responseCode);
    }

    public int getResponseCode() {
        return this.responseCode;
    }

    public void setResponseCode(int responseCode){
        this.responseCode = responseCode;
    }

    @Override
    public String toString() {
        return "(RapResponse) ResponseCode: "+this.responseCode;
    }
}

