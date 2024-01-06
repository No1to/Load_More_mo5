package com.example.load_more_mo5

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewAdapter(var list: ArrayList<Data>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_DATA = 0
        private const val TYPE_PROGRESS = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_DATA -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.row, parent, false)
                DataViewHolder(view)
            }
            TYPE_PROGRESS ->{
                val view = LayoutInflater.from(parent.context).inflate(R.layout.progress_bar, parent, false)
                ProgressHolder(view)
            }
            else -> throw IllegalArgumentException("Different View Type")
        }
    }

    inner class ProgressHolder(itemview: View?) : RecyclerView.ViewHolder(itemview!!)

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textViewTitle = itemView.findViewById<TextView>(R.id.title)
        var textViewSubtitle = itemView.findViewById<TextView>(R.id.subtitle)

        init {
            itemView.setOnClickListener {
                Toast.makeText(itemView.context, "Clicked item: ${list[adapterPosition].subtitle}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is DataViewHolder){
            holder.textViewTitle.text = list[position].title
            holder.textViewSubtitle.text = list[position].subtitle
        }
    }

    override fun getItemViewType(position: Int): Int {
        var viewType =list.get(position).category

        return when(viewType){
            "data" -> TYPE_DATA
            else -> TYPE_PROGRESS
        }
    }
}