package com.kirtan.shah.taskmanager.admin

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TaskStackBuilder
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.text.format.DateFormat
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.Task
import com.google.android.material.button.MaterialButton
import com.google.firebase.database.*
import com.kirtan.shah.taskmanager.R
import com.kirtan.shah.taskmanager.loginsignup.LoginSignup
import com.kirtan.shah.taskmanager.task.TaskData
import kotlinx.android.synthetic.main.assigntask.*
import java.time.LocalDate
import java.time.LocalTime
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.min

class adminActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener{

    var TAG = "AdminActivity"
    lateinit var signout : MaterialButton
    lateinit var profilename : TextView
    lateinit var spinner : Spinner
    lateinit var getdata : MaterialButton
    lateinit var assigntask : MaterialButton
    var itemSelected : String = ""
    var itemselectedIndex=-1
    var empemail = ArrayList<String>()
    var completedtask = ArrayList<TaskData>()
    var empids = ArrayList<String>()
    lateinit var database: DatabaseReference
    lateinit var databasetasks : DatabaseReference
    lateinit var assigntaskedit : EditText
    lateinit var recycler : RecyclerView

    var taskString : String = ""
    var taskstartdt : String = ""
    var taskenddt : String = ""
    var sample: String=""

    lateinit var  startdt : TextView
    lateinit var enddt : TextView
    lateinit var pickstartdt : MaterialButton
    lateinit var  pickenddt : MaterialButton
    lateinit var taskdata : TaskData

    // Calender Vars
    var day = 0
    var month: Int = 0
    var year: Int = 0
    var hour: Int = 0
    var minute: Int = 0
    var myDay = 0
    var myMonth: Int = 0
    var myYear: Int = 0
    var myHour: Int = 0
    var myMinute: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        recycler=findViewById<RecyclerView>(R.id.adminRecyclerView)

        database = FirebaseDatabase.getInstance().getReference().child("Employee")
        databasetasks = FirebaseDatabase.getInstance().getReference().child("Employee")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                empemail = ArrayList<String>()
                empids = ArrayList<String>()
                //Toast.makeText(applicationContext, "Children : ${snapshot.childrenCount}", Toast.LENGTH_LONG).show()

                for(dss : DataSnapshot in snapshot.children)
                {
                    //Toast.makeText(applicationContext, "${dss.child("user").getValue()}", Toast.LENGTH_LONG).show()
                    empemail.add(dss.child("user").getValue().toString())
                    empids.add(dss.key.toString())
                }
                initSpinner()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })


//        empemail = ArrayList<String>()
//        empemail.add("Hello")
//        empemail.add("Kirtan")
        spinner = findViewById<Spinner>(R.id.adminspinner)
        //initEmpArrayData()
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, id: Long) {
                if(position<empemail.size)
                {
                    itemSelected = empemail[position]
                    itemselectedIndex = position
                }

            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                try{
                    if(itemSelected!="" && itemselectedIndex!=-1) {
                        itemSelected = empemail[0]
                        itemselectedIndex = 0
                    }
                }
                catch (e: Exception)
                {
                    Log.e(TAG,"Error in Spinner:${e.message}")
                    e.printStackTrace()
                }
            }

        }
//        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item,empemail)
//        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        spinner.adapter = aa
        getdata = findViewById<MaterialButton>(R.id.buttonGetData)
        getdata.setOnClickListener {
            //Toast.makeText(this, "Data Collected: ${empemail}", Toast.LENGTH_LONG).show()
            Toast.makeText(this, "Selected:${itemSelected}", Toast.LENGTH_LONG).show()
            if(itemSelected!="" && itemselectedIndex!=-1) {
                setEmpTask()
            }

        }

        assigntask = findViewById<MaterialButton>(R.id.adminAssignTask)
        assigntask.setOnClickListener{
            if(itemSelected!="")
            {    assignPopUp()   }
        }

        profilename = findViewById<TextView>(R.id.adminProfileName)
        profilename.text = setProfileName()

        signout = findViewById(R.id.adminSignout)
        signout.setOnClickListener {

            setAdminLoginOut()
            val myIntent = Intent(applicationContext,LoginSignup::class.java
            )
            Toast.makeText(this, "Signed Out", Toast.LENGTH_LONG).show()
            startActivity(myIntent)
            finish()

        }


    }

    override fun onStart() {
        super.onStart()

        initSpinner()
    }

    fun setAdminLoginOut() {
        val pref = applicationContext.getSharedPreferences(
            "myPrefs",
            AppCompatActivity.MODE_PRIVATE
        )
        val editor = pref.edit()
        editor.putBoolean("isLogin", false)
        editor.putString("name", "")
        editor.putString("email", "")
        editor.putBoolean("isEmp", false)
        editor.putBoolean("isAdmin", false)
        editor.apply()
    }

    fun setProfileName() : String?
    {
        val pref = applicationContext.getSharedPreferences("myPrefs", AppCompatActivity.MODE_PRIVATE)
        return pref.getString("name", "Admin")

    }

    private fun initEmpArrayData()
    {
        Toast.makeText(applicationContext, "In Method", Toast.LENGTH_LONG).show()

        var postListener = object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                empemail = ArrayList<String>()
                Toast.makeText(applicationContext, "Children : ${dataSnapshot.childrenCount}", Toast.LENGTH_LONG).show()

                for(dss : DataSnapshot in dataSnapshot.children)
                {
                    Toast.makeText(applicationContext, "${dss.child("user").getValue()}", Toast.LENGTH_LONG).show()
                    empemail.add(dss.child("user").getValue().toString())
                }
                initSpinner()

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
        database.addListenerForSingleValueEvent(postListener)
        //database.addValueEventListener(postListener)
    }

    fun setEmpTask()
    {
        databasetasks.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                completedtask = ArrayList<TaskData>()
                completedtask.clear()
                //empids = ArrayList<String>()
                //Toast.makeText(applicationContext, "Children : ${snapshot.childrenCount}", Toast.LENGTH_LONG).show()

                for(dss : DataSnapshot in snapshot.children)
                {
                    if(dss.child("user").getValue().toString().equals(itemSelected))
                    {
                        for(task : DataSnapshot in dss.child("task").children)
                        {
                            //Toast.makeText(applicationContext, "Adding Task:${task.child("taskname").getValue().toString()}", Toast.LENGTH_SHORT).show()
                            var Tdata : TaskData = TaskData(task.child("taskid").getValue().toString(),
                                task.child("taskname").getValue().toString(),
                                task.child("startdate").getValue().toString(),
                                task.child("enddate").getValue().toString(),
                                task.child("completed").getValue().toString().toBoolean())
                            completedtask.add(Tdata)
                        }
                    }

                }
                recycler=findViewById<RecyclerView>(R.id.adminRecyclerView)
                var adapter = RecyclerAdapter()

                //Toast.makeText(applicationContext, "Total Tasks:${completedtask.size}", Toast.LENGTH_LONG).show()
                adapter.setContentList(completedtask)
                recycler.also { recycler ->
                    recycler.adapter = adapter
                }

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }


    fun initSpinner()
    {
        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item,empemail)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = aa
    }

    fun setTaskToFirebase(taskname:String,taskstdt:String,taskendt:String)
    {
        try{
            var taskid : String = database.child(empids.get(itemselectedIndex)).child("task").push().key.toString()
            var taskd:TaskData= TaskData(taskid,taskname,taskstdt,taskendt,false)
            database.child(empids.get(itemselectedIndex)).child("task").child(taskid).setValue(taskd).addOnCompleteListener { datab->
                //child(name).setValue(UserData).addOnCompleteListener { datab->
                if(datab.isSuccessful)
                {
                    Toast.makeText(this,"Task Added to Emp:)", Toast.LENGTH_LONG).show()
                }
                else
                {
                    Toast.makeText(this,"Error in Task Addition", Toast.LENGTH_LONG).show()
                }
            }

        }
        catch(ex : java.lang.Exception )
        {
            Toast.makeText(this, "SomeThing Wrong:${ex.message}", Toast.LENGTH_LONG).show()
        }
    }

    fun assignPopUp()
    {
        //start Popup
        var dialogStart = Dialog(this)
        dialogStart.show()
        dialogStart.setCanceledOnTouchOutside(true)
        dialogStart.setContentView(R.layout.assigntask)
        assigntaskedit = dialogStart.findViewById<EditText>(R.id.assignedittask)
        pickstartdt = dialogStart.findViewById<MaterialButton>(R.id.pickstartdt)
        pickenddt = dialogStart.findViewById<MaterialButton>(R.id.pickenddt)
        startdt = dialogStart.findViewById<TextView>(R.id.assignstardt)
        enddt = dialogStart.findViewById<TextView>(R.id.assignenddt)

        pickstartdt.setOnClickListener {
//            val timePicker: TimePickerDialog = TimePickerDialog(
//                this,
//                timePickerDialogListener,
//                12,
//                10,
//                false
//            )
//            timePicker.show()
            val calendar: Calendar = Calendar.getInstance()
            day = calendar.get(Calendar.DAY_OF_MONTH)
            month = calendar.get(Calendar.MONTH)
            year = calendar.get(Calendar.YEAR)
            val datePickerDialog =
                DatePickerDialog(this@adminActivity, this@adminActivity, year, month,day)
            sample="1"
            datePickerDialog.show()
        }
        pickenddt.setOnClickListener {
//            val timePicker: TimePickerDialog = TimePickerDialog(
//                this,
//                timePickerDialogListenerendt,
//                12,
//                10,
//                false
//            )
//            timePicker.show()
            val calendar: Calendar = Calendar.getInstance()
            day = calendar.get(Calendar.DAY_OF_MONTH)
            month = calendar.get(Calendar.MONTH)
            year = calendar.get(Calendar.YEAR)
            val datePickerDialog =
                DatePickerDialog(this@adminActivity, this@adminActivity, year, month,day)
            sample="2"
            datePickerDialog.show()
        }
        val btnStart: Button = dialogStart.findViewById<MaterialButton>(R.id.btnAssign)
        btnStart.setOnClickListener { v: View? ->

            taskString = assigntaskedit.text.toString()
            try {
                if (ValidateTask()) {
                    Toast.makeText(this, "Task Assigned", Toast.LENGTH_LONG).show()
                    setTaskToFirebase(assigntaskedit.text.toString(),startdt.text.toString(),enddt.text.toString())
                }
                else
                {
                    Toast.makeText(this,"Please Enter Valid Details",Toast.LENGTH_LONG).show()
                }
            }
            catch (ex:Exception)
            {
                ex.printStackTrace()
                Toast.makeText(this,"Something is wrong:${ex.message}",Toast.LENGTH_SHORT).show()
            }
            dialogStart.dismiss()
        }
    }

    fun ValidateTask() : Boolean
    {
        var isStr : Boolean = assigntaskedit.text.length>0 && startdt.text.length>11 && enddt.text.length>11

        if(isStr)
        {
            var stdt:String = startdt.text.toString()
            var endt:String = enddt.text.toString()
            var startdate:String = stdt.split("T")[0]
            var starttime:String = stdt.split("T")[1]
            var startdatelist : List<String> = startdate.split("-")
            var starttimelist : List<String> = starttime.split(":")

            var enddate:String = endt.split("T")[0]
            var endtime:String = endt.split("T")[1]
            var enddatelist : List<String> = enddate.split("-")
            var endtimelist : List<String> = endtime.split(":")

            var ld1 : LocalDate = LocalDate.of(startdatelist[2].toInt(),startdatelist[1].toInt(),startdatelist[0].toInt())
            var ld2 : LocalDate = LocalDate.of(enddatelist[2].toInt(),enddatelist[1].toInt(),enddatelist[0].toInt())
            var b1 : Int = ld1.compareTo(ld2)
            var lt1 : LocalTime = LocalTime.of(starttimelist[0].toInt(),starttimelist[1].toInt())
            var lt2 : LocalTime = LocalTime.of(endtimelist[0].toInt(),endtimelist[1].toInt())
            var b2 : Int = lt1.compareTo(lt2)
            if(b1<=0 && b2<0)
            {
                return true
            }
        }
        else
        {
            //Toast.makeText(this,"Please Enter Valid Details",Toast.LENGTH_LONG).show()
            return false
        }

        return false

    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

        myDay = dayOfMonth
        myYear = year
        myMonth = month+1
        val calendar: Calendar = Calendar.getInstance()
        hour = calendar.get(Calendar.HOUR)
        minute = calendar.get(Calendar.MINUTE)
        val timePickerDialog = TimePickerDialog(this@adminActivity, this@adminActivity, hour, minute,
            DateFormat.is24HourFormat(this))
        timePickerDialog.show()

    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        myHour = hourOfDay
        myMinute = minute
        if(sample=="1")
        {
            startdt.text="${myDay}-${myMonth}-${myYear}T${myHour}:${myMinute}:00"
        }
        else if(sample=="2")
        {
            enddt.text="${myDay}-${myMonth}-${myYear}T${myHour}:${myMinute}:00"
        }
    }

}

//private val timePickerDialogListenerendt: TimePickerDialog.OnTimeSetListener =
//    object : TimePickerDialog.OnTimeSetListener {
//        override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
//
//            // logic to properly handle
//            // the picked timings by user
//            val formattedTime: String = when {
//                hourOfDay == 0 -> {
//                    if (minute < 10) {
//                        "${hourOfDay + 12}:0${minute} am"
//                    } else {
//                        "${hourOfDay + 12}:${minute} am"
//                    }
//                }
//                hourOfDay > 12 -> {
//                    if (minute < 10) {
//                        "${hourOfDay - 12}:0${minute} pm"
//                    } else {
//                        "${hourOfDay - 12}:${minute} pm"
//                    }
//                }
//                hourOfDay == 12 -> {
//                    if (minute < 10) {
//                        "${hourOfDay}:0${minute} pm"
//                    } else {
//                        "${hourOfDay}:${minute} pm"
//                    }
//                }
//                else -> {
//                    if (minute < 10) {
//                        "${hourOfDay}:${minute} am"
//                    } else {
//                        "${hourOfDay}:${minute} am"
//                    }
//                }
//            }
//
//            sample = formattedTime.toString()
//            enddt.text=sample.toString()
//
//        }
//    }
