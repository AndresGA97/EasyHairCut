package com.example.easyhaircut

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DateAdapter(dateList: ArrayList<DateItem>): RecyclerView.Adapter<DateAdapter.DateViewHolder>() {

    private val dateArrayList:ArrayList<DateItem>

    class DateViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        var dateTextView: TextView
        var nameTextView: TextView

        init {
            dateTextView=itemView.findViewById(R.id.textViewDate)
            nameTextView=itemView.findViewById(R.id.textViewName)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateViewHolder {
        var v:View=
            LayoutInflater.from(parent.context).inflate(R.layout.dates_item, parent, false)
        return DateViewHolder(v)
    }

    override fun getItemCount(): Int {
        return dateArrayList.size
    }

    override fun onBindViewHolder(holder: DateViewHolder, position: Int) {
        val currentItem:DateItem=dateArrayList[position]
        holder.dateTextView.setText(currentItem.date)
        holder.nameTextView.setText(currentItem.name)
    }

    init {
        this.dateArrayList=dateList
    }
}