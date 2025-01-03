package com.transition.ora

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class FareCrowdSrcAdapter (
    private val context: Context,
    private val fareList: List<FareFirestore>
) : BaseAdapter() {
    override fun getCount(): Int {
        return fareList.size
    }

    override fun getItem(position: Int): FareFirestore {
        return fareList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val holder: MyViewHolder

        if (convertView == null) {
            val itemView = LayoutInflater.from(context).inflate(R.layout.crowd_src_fare_row, parent, false)
            holder = MyViewHolder(itemView)
            itemView.tag = holder
        } else {
            holder = convertView.tag as MyViewHolder
        }

        val fare = getItem(position)
        holder.fareNameTv.text = fare.name

        return holder.itemView
    }

    private class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val fareNameTv: TextView = itemView.findViewById(R.id.fareNameTv)
    }
}