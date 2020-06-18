package com.example.easyhaircut

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class DateAdapter(dateList: ArrayList<DateItem>, onDateListener: OnDateListener): RecyclerView.Adapter<DateAdapter.DateViewHolder>() {

    private val dateArrayList:ArrayList<DateItem> = dateList
    private var onDateListener:OnDateListener= onDateListener



    class DateViewHolder(itemView:View, paramOnDateListener: OnDateListener):RecyclerView.ViewHolder(itemView){
        var dateTextView: TextView = itemView.findViewById(R.id.textViewDate)
        var nameTextView: TextView = itemView.findViewById(R.id.textViewName)
        private var deleteButton: Button = itemView.findViewById(R.id.buttonDelete)
        var onDateListener= paramOnDateListener

        init {
            deleteButton.setOnClickListener(View.OnClickListener {
                if (onDateListener != null) {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        onDateListener.onDeleteClick(position)
                    }
                }
            })
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateViewHolder {
        var v:View=
            LayoutInflater.from(parent.context).inflate(R.layout.dates_item, parent, false)
        return DateViewHolder(v, onDateListener)
    }

    override fun getItemCount(): Int {
        return dateArrayList.size
    }

    override fun onBindViewHolder(holder: DateViewHolder, position: Int) {
        val currentItem:DateItem=dateArrayList[position]
        holder.dateTextView.setText(currentItem.date)
        holder.nameTextView.setText(currentItem.name)
    }
    interface OnDateListener{
        fun onDeleteClick(position:Int){}
    }

}