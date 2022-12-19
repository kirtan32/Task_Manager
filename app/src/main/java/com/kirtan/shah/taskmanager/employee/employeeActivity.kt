package com.kirtan.shah.taskmanager.employee

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.kirtan.shah.taskmanager.R
import com.kirtan.shah.taskmanager.task.TaskData


class employeeActivity : AppCompatActivity() {

    lateinit var navigationView: BottomNavigationView

    companion object {
        lateinit var database: DatabaseReference
        lateinit var empid: String
        lateinit var empemail: String
        lateinit var empname: String
        lateinit var allemptask : ArrayList<TaskData>
        lateinit var allemptaskids : ArrayList<String>
        lateinit var remainemptask : ArrayList<TaskData>
        lateinit var completeemptask : ArrayList<TaskData>

        fun InitAllEmpData()
        {

            database.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    allemptask = ArrayList<TaskData>()
                    remainemptask = ArrayList<TaskData>()
                    completeemptask = ArrayList<TaskData>()
                    allemptaskids = ArrayList<String>()
                    //Toast.makeText(this, "Children : ${snapshot.childrenCount}", Toast.LENGTH_LONG).show()

                    for(dss : DataSnapshot in snapshot.children)
                    {
                        if(dss.child("user").getValue().toString().equals(empemail))
                        {
                            empid = dss.key.toString()
                            empname = dss.child("name").getValue().toString()
                            for(task : DataSnapshot in dss.child("task").children)
                            {
                                //Toast.makeText(applicationContext, "Searching Task:${task.child("taskname").getValue().toString()}", Toast.LENGTH_SHORT).show()

                                val taskdata : TaskData? = task.getValue(TaskData::class.java)
                                allemptaskids.add(task.key.toString())
                                allemptask.add(taskdata as TaskData)
                                if(taskdata.completed)
                                {
                                    completeemptask.add(taskdata)
                                }
                                else
                                {
                                    remainemptask.add(taskdata)
                                }
                            }
                        }

                    }

                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
        }


    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employee)

        database = FirebaseDatabase.getInstance().getReference().child("Employee")
        empid=""
        empname=""
        empemail=""
        allemptask = ArrayList<TaskData>()
        remainemptask = ArrayList<TaskData>()
        completeemptask = ArrayList<TaskData>()
        allemptaskids = ArrayList<String>()
        //databasetasks = FirebaseDatabase.getInstance().getReference().child("Employee")


        navigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        supportFragmentManager.beginTransaction().replace(R.id.body_container, EmployeeHomeFragment())
            .commit()
        navigationView.setSelectedItemId(R.id.nav_home)

        navigationView.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            var fragment: Fragment? = null
            when (item.itemId) {
                R.id.nav_home -> fragment = EmployeeHomeFragment()
                R.id.nav_calender -> fragment = EmployeeCalenderFragment()
                R.id.nav_profile -> fragment = EmpProfileFragment()
            }
            if (fragment != null) {
                supportFragmentManager.beginTransaction().replace(R.id.body_container, fragment)
                    .commit()
            }
            true
        })

        //Getting all data from Firebase
        InitEmpEmail()
        InitAllEmpData()
        //displayMsgToast()
    }

    fun InitEmpEmail()
    {
        val pref = this.getSharedPreferences("myPrefs", AppCompatActivity.MODE_PRIVATE)
        empemail = pref.getString("email","Default").toString()

    }

    fun displayMsgToast()
    {
        Toast.makeText(applicationContext, "allemptask : ${allemptask.size}, " +
                "remainemptask:${remainemptask.size}, " +
                "completeEmpTask:${completeemptask.size}, " +
                "allEmpTaskIds: ${allemptaskids.size}," +
                "EmpMail: ${empemail}," +
                "EmpName: ${empname}," +
                "EmpID: ${empid}", Toast.LENGTH_LONG).show()
    }
}