package com.udacity

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationCompat



private val NOTIFICATION_ID = 0
private val REQUEST_CODE = 0
private val FLAGS = PendingIntent.FLAG_ONE_SHOT


fun NotificationManager.sendNotification(messageBody: String, applicationContext: Context, extras: Bundle?) {


    val contentIntent = Intent(applicationContext, MainActivity::class.java)



    val contentPendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )


    val bigtextStyle = NotificationCompat.BigTextStyle()
        .bigText("this is so long text  this is so long text this is so long text this is so long text" +
                " this is so long text this is so long text this is so long text this is so long text")




    val detailIntent = Intent(applicationContext, DetailActivity::class.java).putExtras(extras!!)
    val detailPendingIntent: PendingIntent = PendingIntent.getActivity(
        applicationContext,
        REQUEST_CODE,
        detailIntent,
        FLAGS
    )


    val builder = NotificationCompat.Builder(
        applicationContext,
        applicationContext.getString(R.string.project3_notification_channel_id)
    )



        .setSmallIcon(R.drawable.ic_assistant_black_24dp)
        .setContentTitle("Udacity android kotlin nanodegree")
        .setContentText(messageBody)
        .setStyle(bigtextStyle)



        .setContentIntent(contentPendingIntent)
        .setAutoCancel(true)




        .addAction(
            R.drawable.ic_assistant_black_24dp,
            applicationContext.getString(R.string.check_status),
            detailPendingIntent
        )





    notify(NOTIFICATION_ID, builder.build())

}

