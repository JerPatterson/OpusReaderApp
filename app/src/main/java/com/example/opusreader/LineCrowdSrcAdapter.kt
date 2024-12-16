package com.example.opusreader

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class LineCrowdSrcAdapter (
    private val context: Context,
    private val lineList: List<LineFirestore>
) : BaseAdapter() {
    override fun getCount(): Int {
        return lineList.size
    }

    override fun getItem(position: Int): LineFirestore {
        return lineList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val holder: MyViewHolder

        if (convertView == null) {
            val itemView = LayoutInflater.from(context).inflate(R.layout.crowd_src_line_row, parent, false)
            holder = MyViewHolder(itemView)
            itemView.tag = holder
        } else {
            holder = convertView.tag as MyViewHolder
        }

        val line = getItem(position)
        holder.lineIdTv.text = line.id
        holder.lineNameTv.text = line.name

        val textColor = Color.parseColor(line.textColor)
        holder.lineIdTv.setTextColor(textColor)
        val background = Color.parseColor(line.color)
        holder.lineIdTv.setBackgroundColor(background)

        return holder.itemView
    }

    private class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val lineIdTv: TextView = itemView.findViewById(R.id.lineIdTv)
        val lineNameTv: TextView = itemView.findViewById(R.id.lineNameTv)
    }
}