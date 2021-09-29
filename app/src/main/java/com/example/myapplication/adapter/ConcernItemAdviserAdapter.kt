package com.example.myapplication.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Constant
import com.example.myapplication.DetailConcernActivity
import com.example.myapplication.R
import com.example.myapplication.data.ConcernItemAdviser
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_row_adviser_concerns.view.*

class ConcernItemAdviserAdapter(var context: Context, var list: ArrayList<ConcernItemAdviser>) :
  RecyclerView.Adapter<RecyclerView.ViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    val v: View =
      LayoutInflater.from(context).inflate(R.layout.item_row_adviser_concerns, parent, false)
    return ConcernItemHolder(v)
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    (holder as ConcernItemHolder).bind(
      list[position].id,
      list[position].title,
      list[position].description,
      list[position].postedBy,
      list[position].status,
      list[position].fileName,
      list[position].postedId,
    )
  }

  override fun getItemCount(): Int {
    return list.size
  }

  class ConcernItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(id: String, title: String, description: String, postedBy: String, status: Int, fileName: String, postedId: String) {
      itemView.title_textView.text = title.toUpperCase()
      itemView.description_textView.text = description.toUpperCase()
      itemView.postedBy_textView.text = "Posted By: " + postedBy.toUpperCase()
      itemView.fileNameImage.text = fileName.lowercase()
      Picasso.get().load("${Constant.STATIC_IP}/concern/${postedId}/${fileName}").into(itemView.previewImageView)
      itemView.statusPostChip.text = when(status) {
        1 -> "Completed"
        2 -> "processing"
        else -> {
          "Pending"
        }
      }
      itemView.setOnClickListener {
        val obj = Intent(itemView.context, DetailConcernActivity::class.java)
        obj.putExtra("concernId", id)
        itemView.context.startActivity(obj)
      }
    }
  }
}