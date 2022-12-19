package com.kirtan.shah.taskmanager.employee

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kirtan.shah.taskmanager.R
import com.kirtan.shah.taskmanager.employee.calender.CalendarAdapter
import com.kirtan.shah.taskmanager.employee.calender.CalendarUtils
import com.kirtan.shah.taskmanager.employee.calender.CalendarUtils.daysInWeekArray
import com.kirtan.shah.taskmanager.employee.calender.CalendarUtils.monthYearFromDate
import com.kirtan.shah.taskmanager.employee.calender.Event
import com.kirtan.shah.taskmanager.employee.calender.EventAdapter
import com.kirtan.shah.taskmanager.task.TaskData
import java.time.LocalDate
import java.time.LocalTime

class EmployeeCalenderFragment : Fragment(), CalendarAdapter.OnItemListener  {

    lateinit var monthYearText: TextView
    lateinit var calendarRecyclerView: RecyclerView
    lateinit var eventListView: ListView
    lateinit var prevWeekButton: Button
    lateinit var nextWeekButton: Button
    lateinit var contxt : Context
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_employee_calender, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        CalendarUtils.selectedDate = LocalDate.now()
        contxt=view.context
        initWidgets(view)
        setWeekView()
        setEventsInList()
        setEventAdpater()
    }

    private fun initWidgets(view: View) {
        calendarRecyclerView = view.findViewById<RecyclerView>(R.id.calendarRecyclerView)
        monthYearText = view.findViewById<TextView>(R.id.monthYearTV)
        eventListView = view.findViewById<ListView>(R.id.eventListView)
        prevWeekButton = view.findViewById<Button>(R.id.previousWeek)
        nextWeekButton = view.findViewById<Button>(R.id.nextWeek)
        prevWeekButton.setOnClickListener{
            previousWeekAction(view)
        }
        nextWeekButton.setOnClickListener {
            nextWeekAction(view)
        }
    }

    private fun setWeekView() {
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate as LocalDate))
        val days: ArrayList<LocalDate> = daysInWeekArray(CalendarUtils.selectedDate as LocalDate)

        val calendarAdapter = CalendarAdapter(days, this)
        val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(contxt, 7)
        calendarRecyclerView!!.layoutManager = layoutManager
        calendarRecyclerView!!.adapter = calendarAdapter
        setEventAdpater()
    }

    fun previousWeekAction(view: View?) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate!!.minusWeeks(1)
        setWeekView()
    }

    fun nextWeekAction(view: View?) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate!!.plusWeeks(1)
        setWeekView()
    }

    override fun onItemClick(position: Int, date: LocalDate?) {
        CalendarUtils.selectedDate = date
        setWeekView()
    }

    override fun onResume() {
        super.onResume()
        setEventAdpater()
    }
    private fun setEventAdpater() {
        val dailyEvents: ArrayList<Event> = Event.eventsForDate(CalendarUtils.selectedDate!!)
        val eventAdapter = EventAdapter(contxt, dailyEvents)
        eventListView.adapter = eventAdapter
    }

    fun newEventAction(view: View?) {
        //startActivity(Intent(this, EventEditActivity::class.java))
    }

    private fun setEventsInList()
    {
        Event.eventsList.clear()
        for(emptask : TaskData in employeeActivity.allemptask)
        {
            try{

            }
            catch (ex:Exception)
            {
                ex.printStackTrace()
                Toast.makeText(contxt,"Something Wrong: ${ex.message}",Toast.LENGTH_SHORT).show()
            }
            val eventName = emptask.taskname
            // Start Date Variables
            val datetime : List<String> = emptask.startdate.split("T")
            val stdate : List<String> = datetime[0].split("-")
            val srttime : List<String> = datetime[1].split(":")
            // End Date Variables
            val endttime : List<String> = emptask.enddate.split("T")
            val etdate : List<String> = endttime[0].split("-")
            val endtime : List<String> = endttime[1].split(":")

            val sttime = LocalTime.of(srttime[0].toInt(),srttime[1].toInt())
            val mystartdate = LocalDate.of(stdate[2].toInt() ,stdate[1].toInt(),stdate[0].toInt())

            val ettime = LocalTime.of(endtime[0].toInt(),endtime[1].toInt())
            val myenddate = LocalDate.of(etdate[2].toInt() ,etdate[1].toInt(),etdate[0].toInt())

            val newEvent = Event(eventName, mystartdate, sttime,myenddate,ettime)
            Event.eventsList.add(newEvent)
        }

    }

}