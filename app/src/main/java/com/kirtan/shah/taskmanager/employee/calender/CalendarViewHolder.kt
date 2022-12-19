package com.kirtan.shah.taskmanager.employee.calender

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kirtan.shah.taskmanager.R
import java.time.LocalDate

class CalendarViewHolder(
    itemView: View,
    onItemListener: CalendarAdapter.OnItemListener,
    days: ArrayList<LocalDate>
) :
    RecyclerView.ViewHolder(itemView), View.OnClickListener {
    private val days: ArrayList<LocalDate>
    val parentView: View
    val dayOfMonth: TextView
    private val onItemListener: CalendarAdapter.OnItemListener

    init {
        parentView = itemView.findViewById(R.id.parentView)
        dayOfMonth = itemView.findViewById(R.id.cellDayText)
        this.onItemListener = onItemListener
        itemView.setOnClickListener(this)
        this.days = days
    }

    override fun onClick(view: View) {
        onItemListener.onItemClick(adapterPosition, days[adapterPosition])
    }
}