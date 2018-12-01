package com.dysplace.dysplaceproto1.pojos.lambda_event_generator.get_new_asset_ids;

import com.dysplace.dysplaceproto1.pojos.lambda_event_generator.LatLng;

//Get New Asset IDs Request

public class GnaiRequest {

    private LatLng latLng;

    public GnaiRequest(LatLng latLng){
        this.setLatLng(latLng);
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    @Override
    public String toString() {
        return "(GnaiRequest) "+latLng.toString();
    }
}
