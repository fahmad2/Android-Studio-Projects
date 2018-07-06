package com.dysplace.prismmobile.boot_receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.dysplace.prismmobile.main.MainActivity;

public class StartActivityAtBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())){
            Intent mainActivityIntent = new Intent(context, MainActivity.class);
            context.startActivity(mainActivityIntent);
        }
    }
}
