package com.dysplace.dysplaceproto1.pojos.lambda_event_generator.get_new_asset_ids;

import com.dysplace.dysplaceproto1.pojos.lambda_event_generator.AssetData;

import java.util.Arrays;
import java.util.List;

// Get New Asset IDs Response

public class GnaiResponse {

    private int responseCode;
    private List<AssetData> listAssetData;

    public GnaiResponse(int responseCode, List<AssetData> listAssetData) {
        this.setResponseCode(responseCode);
        this.setListAssetData(listAssetData);
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public List<AssetData> getListAssetData() {
        return listAssetData;
    }

    public void setListAssetData(List<AssetData> listAssetData) {
        this.listAssetData = listAssetData;
    }

    @Override
    public String toString() {
        return "(GnaiResponse) ResponseCode: "+this.responseCode+", ListAssetData: "+
                Arrays.toString(listAssetData.toArray());
    }
}
