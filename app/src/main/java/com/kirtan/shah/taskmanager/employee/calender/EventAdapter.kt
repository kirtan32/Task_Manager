package com.kirtan.shah.taskmanager.employee.calender

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.kirtan.shah.taskmanager.R

class EventAdapter(context: Context, events: List<Event?>?) :
    ArrayAdapter<Event?>(context, 0, events!!) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val event = getItem(position)
        if (convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.event_cell, parent, false)
        }
        val eventCellTV = convertView!!.findViewById<TextView>(R.id.eventCellTV)
        val eventdt = convertView.findViewById<TextView>(R.id.eventdt)
        val eventTitle: String = event?.name ?:" Event Name"
            (event?.name ?: "Event Name") + " " + event?.let { CalendarUtils.formattedTime(it.sttime) }
        val eventtime : String =(event?.date.toString()?:"Start Date")+" , " + (event?.sttime.toString()?:"Start Time") + "   To   " + (event?.endate.toString()?:"End Date") +" , "+ (event?.endtime.toString()?:"End Time")
        eventCellTV.text = eventTitle
        eventdt.text = eventtime
        return convertView
    }
}