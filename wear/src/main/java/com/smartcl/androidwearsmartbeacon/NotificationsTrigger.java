package com.smartcl.androidwearsmartbeacon;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

/**
 * Trigger a notification on the wearable device.
 * This class displays the notification as well as different pages (questions)
 * and several actions.
 */
public class NotificationsTrigger {

    public static void TriggerNotification(Context packageContext, String question) {

        //TODO: get the question from the parameters
        Notification questionNotif = buildQuestionNotification(packageContext, question);

        Intent websiteIntent = new Intent(packageContext, WebsiteIntentService.class);
        PendingIntent websitePendingIntent = PendingIntent
                .getService(packageContext, 0, websiteIntent, 0);

        //TODO: put the string in string.xml file
        Notification notification =
                new NotificationCompat.Builder(packageContext)
                        .setSmallIcon(R.drawable.ic_full_sad)
                        .setContentTitle("Title")
                        .setContentText("Swipe to the right!")
                        .addAction(R.drawable.ic_launcher,
                                   packageContext.getString(R.string.open_lcl_website_title),
                                   websitePendingIntent)
                        .extend(new NotificationCompat.WearableExtender().addPage(questionNotif))
                        .setVibrate(new long[]{1000, 1000})
                        .build();


        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(packageContext);

        notificationManager.notify(001, notification);
    }

    private static Notification buildQuestionNotification(Context packageContext, String question) {
        Intent questionIntent = new Intent(packageContext, NotificationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("question", question);
        questionIntent.putExtras(bundle);

        PendingIntent questionPendingIntent =
                PendingIntent.getActivity(packageContext, 0, questionIntent,
                                          PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notificationQuestion =
                new NotificationCompat.Builder(packageContext)
                        .setSmallIcon(R.drawable.ic_full_sad)
                        .extend(new NotificationCompat.WearableExtender()
                                        .setDisplayIntent(questionPendingIntent)
                                        .setCustomSizePreset(
                                                NotificationCompat.WearableExtender.SIZE_LARGE))
                        .build();
        return notificationQuestion;
    }

}
