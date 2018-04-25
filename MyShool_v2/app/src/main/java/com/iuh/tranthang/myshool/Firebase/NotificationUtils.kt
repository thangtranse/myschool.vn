package com.iuh.tranthang.myshool.Firebase

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.support.v4.app.NotificationCompat
import android.text.TextUtils
import android.util.Log
import com.iuh.tranthang.myshool.R
import java.text.ParseException
import java.text.SimpleDateFormat

/**
 * Created by ThinkPad on 4/25/2018.
 */
class NotificationUtils {
    var context: Context? = null

    constructor(context: Context?) {
        this.context = context
    }

    public fun showNotificationMessage(title: String, message: String, timeStamp: String, intent: Intent) {
        // Check for empty push message
        if (TextUtils.isEmpty(message)) {
            Log.e("tmt", "empty")
            return
        }
        // notification icon
        val icon = R.mipmap.ic_launcher
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP

        val resultPendingIntent = PendingIntent.getActivity(
                context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)
        val mBuilder = NotificationCompat.Builder(context)
        val alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                + "://" + context!!.getPackageName() + "/raw/notification")

        showSmallNotification(mBuilder, icon, title, message, timeStamp, resultPendingIntent, alarmSound)
    }

    private fun showSmallNotification(mBuilder: NotificationCompat.Builder, icon: Int, title: String, message: String, timeStamp: String, resultPendingIntent: PendingIntent?, alarmSound: Uri?) {
        val inboxStyle = NotificationCompat.InboxStyle()
        inboxStyle.addLine(message)
        val notification: Notification
        notification = mBuilder.setSmallIcon(icon).setTicker(title).setWhen(0)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentIntent(resultPendingIntent)
                .setSound(alarmSound)
                .setStyle(inboxStyle)
                .setWhen(getTimeMilliSec(timeStamp))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(context!!.getResources(), icon))
                .setContentText(message)
                .build()

        val notificationManager = context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(100, notification)
    }

    fun getTimeMilliSec(timeStamp: String): Long {
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        try {
            val date = format.parse(timeStamp)
            return date.time
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return 0
    }
}