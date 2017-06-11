package com.harryio.flubo.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.harryio.flubo.service.AlarmHelperService;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        AlarmHelperService.startActionCreateAllAlarms(context);
    }
}