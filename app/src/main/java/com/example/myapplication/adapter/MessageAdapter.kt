package com.example.myapplication.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.MessageDetailActivity
import com.example.myapplication.R
import com.example.myapplication.data.MessageData
import kotlinx.android.synthetic.main.item_row_messages.view.*
import kotlin.collections.ArrayList

class MessageAdapter(var context: Context, private var list: ArrayList<MessageData>) :
  RecyclerView.Adapter<RecyclerView.ViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    val v: View = LayoutInflater.from(context).inflate(R.layout.item_row_messages, parent, false)
    return MessageItemHolder(v)
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    (holder as MessageItemHolder).bind(
      list[position].id,
      list[position].name,
      list[position].sendBy,
      list[position].receivedBy,
      list[position].content,
      list[position].status,
      list[position].readAt,
      list[position].createdAt,
      list[position].updatedAt,
      )
  }

  override fun getItemCount(): Int {
    return list.size
  }

  class MessageItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(
      id: String,
      name: String,
      sendBy: String,
      receivedBy: String,
      content: String,
      status: Boolean,
      readAt: String,
      createdAt: String,
      updatedAt: String,
    ) {
      itemView.senderTextView.text = name
      itemView.contentTextView.text = content
      itemView.datePublishedTextView.text = createdAt

      if (status) {
        itemView.statusTextView.visibility = View.GONE
      } else {
        itemView.statusTextView.visibility = View.VISIBLE
      }

      itemView.setOnClickListener {
        val obj = Intent(itemView.context, MessageDetailActivity::class.java)
        obj.putExtra("id", id)
        obj.putExtra("sendBy", sendBy)
        obj.putExtra("name", name)
        obj.putExtra("receivedBy", receivedBy)
        obj.putExtra("content", content)
        itemView.context.startActivity(obj)
      }
    }
  }
}