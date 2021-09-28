//package com.example.myapplication//package com.example.myapplication.service
////
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.content.Intent;
//import android.support.v4.app.NotificationCompat;
//
//import com.google.firebase.messaging.RemoteMessage;
//
//
//open class FirebaseMessagingService : com.google.firebase.messaging.FirebaseMessagingService() {
//
//  override fun onMessageReceived(remoteMessage: RemoteMessage) {
//    remoteMessage.data.get("message")?.let { showNotification(it) }
//  }
//
//  private fun showNotification(message: String) {
//    val i = Intent(this, MainActivity::class.java)
//    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//    val pendingIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT)
//    val builder: NotificationCompat.Builder = NotificationCompat.Builder(this)
//      .setAutoCancel(true)
//      .setContentTitle("FCM Test")
//      .setContentText(message)
//      .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
//      .setContentIntent(pendingIntent)
//    val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager?
//    manager!!.notify(0, builder.build())
//  }
//}