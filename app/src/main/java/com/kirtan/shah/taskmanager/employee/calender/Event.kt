package com.kirtan.shah.taskmanager.employee.calender

import android.util.Log
import android.widget.Toast
import java.time.LocalDate
import java.time.LocalTime

class Event(var name: String, var date: LocalDate, var sttime: LocalTime,var endate:LocalDate,var endtime:LocalTime) {

    companion object {
        var eventsList = ArrayList<Event>()
        fun eventsForDate(date: LocalDate) : ArrayList<Event> {
            val events = ArrayList<Event>()
            for (event in eventsList) {
                Log.d("Events--> ","${event.date} : ${date}")
                if (event.date == date)
                {
                    Log.d("Event","${event.date} : ${date}")
                    events.add(event)
                }
            }
            return events
        }
    }
}