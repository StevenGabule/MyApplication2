package com.example.myapplication.notifications


import android.R
import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.myapplication.MainActivity
import com.example.myapplication.MessageAdviserActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseServices : FirebaseMessagingService() {
  companion object {
    private const val TAG = "PushNotification"
    private const val CHANNEL_ID = "101"
  }

  override fun onMessageReceived(remoteMessage: RemoteMessage) {
    // [START_EXCLUDE]
    // There are two types of messages data messages and notification messages. Data messages are handled
    // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
    // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
    // is in the foreground. When the app is in the background an automatically generated notification is displayed.
    // When the user taps on the notification they are returned to the app. Messages containing both notification
    // and data payloads are treated as notification messages. The Firebase console always sends notification
    // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
    // [END_EXCLUDE]

    // TODO(developer): Handle FCM messages here.
    // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
    Log.d(TAG, "From: ${remoteMessage.from}")

    // Check if message contains a data payload.
    if (remoteMessage.data.isNotEmpty()) {
      Log.d(TAG, "Message data payload: ${remoteMessage.data}")
    }

    // Check if message contains a notification payload.
    remoteMessage.notification?.let {
      Log.d(TAG, "Message Notification Bodysss: ${it.body}")
    }

    // Also if you intend on generating your own notifications as a result of a received FCM
    // message, here is where that should be initiated. See sendNotification method below.
  }

  /**
   * Schedule async work using WorkManager.
   */
  /*private fun scheduleJob() {
    // [START dispatch_job]
    val work = OneTimeWorkRequest.Builder(MyWorker::class.java).build()
    WorkManager.getInstance(this).beginWith(work).enqueue()
    // [END dispatch_job]
  }*/

  /**
   * Handle time allotted to BroadcastReceivers.
   */
  private fun handleNow() {
    Log.d(TAG, "Short lived task is done.")
  }

  private fun showNotification(title: String, message: String) {
    val intent = Intent(this, MainActivity::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
    val builder: NotificationCompat.Builder = NotificationCompat.Builder(this, CHANNEL_ID)
      .setSmallIcon(R.drawable.ic_menu_zoom)
      .setContentTitle(title)
      .setContentText(message)
      .setPriority(NotificationCompat.PRIORITY_DEFAULT) // Set the intent that will fire when the user taps the notification
      .setContentIntent(pendingIntent)
      .setAutoCancel(true)
    val notificationManager = NotificationManagerCompat.from(this)

    // notificationId is a unique int for each notification that you must define
    notificationManager.notify(1, builder.build())
  }


}
