package com.dysplace.dysplaceproto1.interfaces.lambda_event_generator;

import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunction;
import com.dysplace.dysplaceproto1.pojos.lambda_event_generator.get_new_asset_ids.GnaiRequest;
import com.dysplace.dysplaceproto1.pojos.lambda_event_generator.get_new_asset_ids.GnaiResponse;
import com.dysplace.dysplaceproto1.pojos.lambda_event_generator.report_asset_played.RapRequest;
import com.dysplace.dysplaceproto1.pojos.lambda_event_generator.report_asset_played.RapResponse;

public interface LambdaInvokerInterface {

    @LambdaFunction(functionName = "getNewAssetIDs", qualifier = "BETA")
    GnaiResponse invokeGNAI(GnaiRequest gnaiRequest);

    @LambdaFunction(functionName = "reportAssetPlayed", qualifier = "BETA")
    RapResponse invokeRAP(RapRequest rapRequest);
    
}
