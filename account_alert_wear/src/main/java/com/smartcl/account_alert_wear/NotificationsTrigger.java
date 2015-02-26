package com.smartcl.account_alert_wear;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import org.json.simple.JSONObject;

/**
 * Trigger a notification on the wearable device.
 * This class displays the notification as well as different pages (questions)
 * and several actions.
 */
public class NotificationsTrigger {

    public static void TriggerNotification(Context packageContext, JSONObject accountsJson) {
        Notification accountNotif = buildAccountNotification(packageContext, accountsJson);
        PendingIntent accountHistoryIntent = buildAccountHistoryIntent(packageContext, accountsJson);

        //TODO: put the string in string.xml file
        Notification notification =
                new NotificationCompat.Builder(packageContext)
                        .setSmallIcon(R.drawable.icon_bank)
                        .setContentTitle("title")
                        .setContentText("content") //TODO: set content
                        .extend(new NotificationCompat.WearableExtender().addPage(accountNotif))
                        .addAction(R.drawable.icon_bank, packageContext.getString(R.string.account_history), accountHistoryIntent)
                        .setVibrate(new long[]{1000, 1000})
                        .build();

        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(packageContext);

        notificationManager.notify(001, notification);
    }

    private static Notification buildAccountNotification(Context packageContext,
                                                         JSONObject accountsJson) {
        Intent accountIntent = new Intent(packageContext, CurrentAccountActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("account_data", accountsJson.toJSONString());
        accountIntent.putExtras(bundle);

        PendingIntent accountPendingIntent =
                PendingIntent.getActivity(packageContext, 0, accountIntent,
                                          PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notificationAccounts =
                new NotificationCompat.Builder(packageContext)
                        .setSmallIcon(R.drawable.ic_full_sad)
                        .extend(new NotificationCompat.WearableExtender()
                                        .setDisplayIntent(accountPendingIntent)
                                        .setCustomSizePreset(
                                                NotificationCompat.WearableExtender.SIZE_LARGE))
                        .build();
        return notificationAccounts;
    }

    private static PendingIntent buildAccountHistoryIntent(Context packageContext, JSONObject accountsJson) {
        Intent accountIntent = new Intent(packageContext, AccountHistoryActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("account_data", accountsJson.toJSONString());
        accountIntent.putExtras(bundle);

        PendingIntent accountPendingIntent =
                PendingIntent.getActivity(packageContext, 0, accountIntent,
                                          PendingIntent.FLAG_UPDATE_CURRENT);
        return accountPendingIntent;
    }

}
