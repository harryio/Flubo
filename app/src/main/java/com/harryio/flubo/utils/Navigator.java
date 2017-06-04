package com.harryio.flubo.utils;

import android.content.Context;
import android.content.Intent;

import com.harryio.flubo.activity.CreateOrEditReminderActivity;

public class Navigator {
    public static void navigateToCreateReminderActivity(Context context) {
        Intent intent = CreateOrEditReminderActivity.getCallingIntent(context);
        context.startActivity(intent);
    }

    public static void navigateToEditRemiderActivity(Context context, long reminderId) {
        Intent intent = CreateOrEditReminderActivity.getCallingIntent(context, reminderId);
        context.startActivity(intent);
    }
}
