package com.example.myapplication.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.DetailConcernActivity
import com.example.myapplication.R
import com.example.myapplication.data.ConcernItemAdviser
import kotlinx.android.synthetic.main.item_row_adviser_concerns.view.*
import kotlinx.android.synthetic.main.item_row_concerns.view.description_textView
import kotlinx.android.synthetic.main.item_row_concerns.view.postedBy_textView
import kotlinx.android.synthetic.main.item_row_concerns.view.title_textView

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
      list[position].fileName
    )
  }

  override fun getItemCount(): Int {
    return list.size
  }

  class ConcernItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(id: String, title: String, description: String, postedBy: String, status: Int, fileName: String) {
      itemView.title_textView.text = title
      itemView.description_textView.text = description
      itemView.postedBy_textView.text = postedBy
      itemView.fileNameImage.text = fileName.lowercase()
      itemView.setOnClickListener {
        val obj = Intent(itemView.context, DetailConcernActivity::class.java)
        obj.putExtra("concernId", id)
        itemView.context.startActivity(obj)
      }
    }
  }
}