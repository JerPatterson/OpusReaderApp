package com.transition.ora.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.transition.ora.R

class TypeCrowdSrcAdapter (
    private val context: Context,
    private val cardTypeVariantList: List<String>
) : BaseAdapter() {
    override fun getCount(): Int {
        return cardTypeVariantList.size
    }

    override fun getItem(position: Int): String {
        return cardTypeVariantList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val holder: MyViewHolder

        if (convertView == null) {
            val itemView = LayoutInflater.from(context).inflate(R.layout.crowd_src_type_row, parent, false)
            holder = MyViewHolder(itemView)
            itemView.tag = holder
        } else {
            holder = convertView.tag as MyViewHolder
        }

        val cardTypeVariant = getItem(position)
        holder.cardTypeVariantNameTv.text = cardTypeVariant

        return holder.itemView
    }

    private class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val cardTypeVariantNameTv: TextView = itemView.findViewById(R.id.cardTypeVariantNameTv)
    }
}