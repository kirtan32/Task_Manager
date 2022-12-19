package com.kirtan.shah.taskmanager.employee.calender

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kirtan.shah.taskmanager.R
import java.time.LocalDate

class CalendarAdapter(
    private var days: ArrayList<LocalDate>,
    private var onItemListener: OnItemListener
) :
    RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarAdapter.CalendarViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.calendar_cell, parent, false)
        val layoutParams = view.layoutParams
        if (days.size > 15) //month view
            layoutParams.height = (parent.height * 0.166666666).toInt()
        else  // week view
            layoutParams.height = parent.height
        return CalendarViewHolder(view, onItemListener, days)
    }

    override fun onBindViewHolder(holder: CalendarAdapter.CalendarViewHolder, position: Int) {
        val date = days[position]
        if (date == null) holder.dayOfMonth.text = ""
        else {
            holder.dayOfMonth.text = date.dayOfMonth.toString()
            if (date == CalendarUtils.selectedDate) holder.parentView.setBackgroundColor(Color.LTGRAY)
        }
    }

    override fun getItemCount(): Int {
        return days.size
    }

    interface OnItemListener {
        fun onItemClick(position: Int, date: LocalDate?)
    }

    inner class CalendarViewHolder(
        itemView: View,
        onItemListener: OnItemListener,
        days: ArrayList<LocalDate>
    ) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val days: ArrayList<LocalDate>
        val parentView: View
        val dayOfMonth: TextView
        private val onItemListener: OnItemListener

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

}
