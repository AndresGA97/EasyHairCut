package com.example.easyhaircut

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class HairdresserAdapter(exampleList: ArrayList<HairdresserItem>, onHairdresserListener: OnHairdresserListener) :
    RecyclerView.Adapter<HairdresserAdapter.HairdresserViewHolder>() {
    private val hairdresserList: ArrayList<HairdresserItem> = exampleList
    private var onHairdresserListener:OnHairdresserListener = onHairdresserListener

    class HairdresserViewHolder(itemView: View, onHairdresserListener: OnHairdresserListener) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var mTextView1: TextView = itemView.findViewById(R.id.hairdresserName)
        var onHairdresserListener:OnHairdresserListener = onHairdresserListener

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            onHairdresserListener.hairdresserClick(adapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HairdresserViewHolder {
        val v: View =
            LayoutInflater.from(parent.context).inflate(R.layout.hairdresser_item, parent, false)
        return HairdresserViewHolder(v, onHairdresserListener)
    }

    override fun onBindViewHolder(holder: HairdresserViewHolder, position: Int) {
        val currentItem: HairdresserItem = hairdresserList[position]
        holder.mTextView1.setText(currentItem.hairdresserName )
    }

    override fun getItemCount(): Int {
        return hairdresserList.size
    }

    interface OnHairdresserListener{
        fun hairdresserClick(position: Int)
    }
}