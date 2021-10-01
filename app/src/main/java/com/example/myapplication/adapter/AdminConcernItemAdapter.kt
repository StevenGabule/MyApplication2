package com.example.myapplication.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Constant.Companion.STATIC_IP
import com.example.myapplication.DetailConcernActivity
import com.example.myapplication.R
import com.example.myapplication.admin.ConcernDetailActivity
import com.example.myapplication.data.AdminConcernItem
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_row_concerns_admin.view.*
import java.util.*

class AdminConcernItemAdapter(var context: Context, var list: ArrayList<AdminConcernItem>) :
  RecyclerView.Adapter<RecyclerView.ViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
     val v:View = LayoutInflater.from(context).inflate(R.layout.item_row_concerns_admin, parent, false)
    return AdminConcernItemHolder(v)
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    (holder as AdminConcernItemHolder).bind(
      list[position].id,
      list[position].title,
      list[position].description,
      list[position].postedBy,
      list[position].status,
      list[position].assignBy,
      list[position].concernFile,
      list[position].createdAt,
      list[position].loadId)
  }

  override fun getItemCount(): Int {
    return list.size
  }

  class AdminConcernItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    @SuppressLint("SetTextI18n")
    fun bind(
      id: String,
      title: String,
      description: String,
      postedBy: String,
      status: Int,
      assignBy: String,
      concernFile: String,
      createdAt: String,
      postedId: String,
    ) {
      itemView.title_textView.text = title.toUpperCase()
      itemView.description_textView.text = description.toUpperCase()
      itemView.postedBy_textView.text = "Posted By: $postedBy on $createdAt".toUpperCase()
      itemView.statusPostChip.text = when(status) {
        1 -> "Completed"
        2 -> "processing"
        else -> {
          "Pending"
        }
      }
      itemView.assignPostChip.text = assignBy
      Picasso.get().load("$STATIC_IP/concern/${postedId}/${concernFile}").into(itemView.previewImageView)

      itemView.setOnClickListener {
        val obj = Intent(itemView.context, ConcernDetailActivity::class.java)
        obj.putExtra("concernId", id)
        obj.putExtra("title", title)
        itemView.context.startActivity(obj)
      }
    }
  }
}