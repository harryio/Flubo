package com.harryio.flubo.utils;

import android.content.Context;
import android.content.Intent;

import com.harryio.flubo.activity.CreateReminderActivity;

public class Navigator {
    public static void navigateToCreateReminderActivity(Context context) {
        Intent intent = CreateReminderActivity.getCallingIntent(context);
        context.startActivity(intent);
    }
}
