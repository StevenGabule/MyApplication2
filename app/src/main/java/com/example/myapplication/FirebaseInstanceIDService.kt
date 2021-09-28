//package com.example.myapplication//package com.example.myapplication.service
//
//import com.google.firebase.iid.FirebaseInstanceId
//import com.google.firebase.iid.FirebaseInstanceIdService
//
//import com.example.myapplication.Constant
//import okhttp3.FormBody
//import okhttp3.OkHttpClient
//import okhttp3.Request
//import okhttp3.RequestBody
//import java.io.IOException
////
//class FirebaseInstanceIDService : FirebaseInstanceIdService() {
//  override fun onTokenRefresh() {
//    val token: String = FirebaseInstanceId.getInstance().getToken()!!
//    registerToken(token)
//  }
//
//  private fun registerToken(token: String) {
//    val client = OkHttpClient()
//    val body: RequestBody = FormBody.Builder()
//      .add("Token", token)
//      .build()
//    val request: Request =  Request.Builder()
//      .url("${Constant.PUBLIC_IP}/notification/create")
//      .post(body)
//      .build()
//    try {
//      client.newCall(request).execute()
//    } catch (e: IOException) {
//      e.printStackTrace()
//    }
//  }
//}