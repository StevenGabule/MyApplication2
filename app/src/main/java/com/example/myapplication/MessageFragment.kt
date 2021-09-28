package com.example.myapplication

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication.adapter.MessageAdapter
import com.example.myapplication.data.MessageData

class MessageFragment : Fragment() {
  private lateinit var messageRecyclerView: RecyclerView
  private val url = "${Constant.PUBLIC_IP}/contact/customer"

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    val view = inflater.inflate(R.layout.fragment_message, container, false)

    messageRecyclerView = view.findViewById(R.id.messagesRecycleView)
    val pref = activity?.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
    val token = pref?.getString("TOKEN", null)

    try {
      val list = ArrayList<MessageData>()
      val rq: RequestQueue = Volley.newRequestQueue(view.context)
      val jar = object : JsonArrayRequest(Method.GET, url, null, { response ->
        Log.d("API_MESSAGE_SUCCESS", "$response")
        for (x in 0 until response.length()) {
          list.add(
            MessageData(
              response.getJSONObject(x).getString("_id"),
              response.getJSONObject(x).getJSONObject("sendBy")["name"].toString(),
              response.getJSONObject(x).getJSONObject("sendBy")["_id"].toString(),
              response.getJSONObject(x).getString("receivedBy"),
              response.getJSONObject(x).getString("content"),
              response.getJSONObject(x).getBoolean("status"),
              response.getJSONObject(x).getString("readAt"),
              response.getJSONObject(x).getString("createdAt"),
              response.getJSONObject(x).getString("updatedAt")
            )
          )
        }
        val adp = MessageAdapter(view.context, list)
        messageRecyclerView.layoutManager = LinearLayoutManager(view.context)
        messageRecyclerView.adapter = adp
      }, { error ->
        Log.e("API_MESSAGE_FAILED", "$error.message")
        Toast.makeText(view.context, error.message, Toast.LENGTH_LONG).show()
      }) {
        override fun getHeaders(): MutableMap<String, String> {
          val params: MutableMap<String, String> = HashMap()
          params["Authorization"] = "Bearer $token"
          return params
        }
      }
      rq.add(jar)
    } catch (e: Exception) {
      Log.e("API_MESSAGE_FAILED", "$e")
    }

    return view
  }

}