package com.kirtan.shah.taskmanager.employee

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.Task
import com.google.android.material.button.MaterialButton
import com.google.firebase.database.*
import com.kirtan.shah.taskmanager.R
import com.kirtan.shah.taskmanager.admin.RecyclerAdapter
import com.kirtan.shah.taskmanager.loginsignup.LoginSignup
import com.kirtan.shah.taskmanager.task.TaskData
import kotlinx.coroutines.*
import java.text.FieldPosition

class EmployeeHomeFragment : Fragment(),ClickHandler {

    lateinit var contxt: Context

    //UI Component
    lateinit var remainRecyclerView: RecyclerView
    lateinit var completeRecyclerView: RecyclerView
    lateinit var remainTextView: TextView
    lateinit var completeTextView: TextView

    //EmpTaskData
    lateinit var emptask : ArrayList<TaskData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_employee_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        contxt = view.context

        remainRecyclerView=view.findViewById<RecyclerView>(R.id.empRemainTaskRecyclerView)
        completeRecyclerView=view.findViewById<RecyclerView>(R.id.empCompleteTaskRecyclerView)
        remainTextView=view.findViewById<TextView>(R.id.remainTextView)
        completeTextView=view.findViewById<TextView>(R.id.completedTextView)

        //Thread.sleep(1500)
        // Setting Up TextView Visibility
        setTextViews()
        // Setting Up Recycler View
        setRemainTasks()
        setCompleteTasks()
    }

    override fun onStart() {
        super.onStart()

    }

    fun setRemainTasks()
    {
        var adapter = EmpRecyclerAdapter(this)

        //Toast.makeText(context, "Total Remain Tasks:${employeeActivity.remainemptask.size}", Toast.LENGTH_LONG).show()
        adapter.setContentList(employeeActivity.remainemptask)
        remainRecyclerView.also { recycler ->
            recycler.adapter = adapter
        }
    }
    fun setCompleteTasks()
    {
        var adapter = EmpCompleteTaskAdapter()

        //Toast.makeText(context, "Total Completed Tasks:${employeeActivity.completeemptask.size}", Toast.LENGTH_LONG).show()
        adapter.setContentList(employeeActivity.completeemptask)
        completeRecyclerView.also { recycler ->
            recycler.adapter = adapter
        }
    }

    fun setTextViews()
    {
        if(employeeActivity.remainemptask.size==0)
        {
            remainTextView.visibility=View.VISIBLE
        }
        else
        {
            remainTextView.visibility=View.INVISIBLE
        }
        if(employeeActivity.completeemptask.size==0)
        {
            completeTextView.visibility=View.VISIBLE
        }
        else
        {
            completeTextView.visibility=View.INVISIBLE
        }
    }

    fun setEmpLoginOut() {
        val pref = requireActivity().application.applicationContext.getSharedPreferences(
            "myPrefs",
            AppCompatActivity.MODE_PRIVATE
        )
        LoginSignup.auth.signOut()
        val editor = pref.edit()
        editor.putBoolean("isLogin", false)
        editor.putString("name", "")
        editor.putString("email", "")
        editor.putBoolean("isEmp", false)
        editor.putBoolean("isAdmin", false)
        editor.apply()
    }

    override fun handleClick(taskData: TaskData, position: Int)
    {
        //Opening the DialogBox
        DialogActivity(taskData,position)

    }

    fun performUpdateOperation(taskData: TaskData, position: Int)
    {
        //IsCompleted Data in Task is setted in HashMap
        var isCompletedMap : HashMap<String,Boolean> = HashMap()
        isCompletedMap.put("completed",true)

        employeeActivity.database.child(employeeActivity.empid).child("task").child(taskData.taskid).updateChildren(
            isCompletedMap as Map<String, Any>
        ).addOnCompleteListener {
            if(it.isSuccessful)
            {
                Toast.makeText(context,"Task Updated", Toast.LENGTH_SHORT).show()
                //Adding task to Completed Array & Removing Task from Remain Array
                var counter:Int=0

//                var tskData : TaskData = employeeActivity.remainemptask.removeAt(counter)
//                employeeActivity.completeemptask.add(taskData)
                employeeActivity.InitAllEmpData()
                setTextViews()
                setRemainTasks()
                setCompleteTasks()
            }
            else
            {
                Toast.makeText(context,"Task Not Updated", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(context,"Task Updated Failed", Toast.LENGTH_SHORT).show()
        }
    }

    fun DialogActivity(taskData: TaskData, position: Int)
    {
        try {
            var dialogStart = Dialog(contxt)
            dialogStart.show()
            dialogStart.setCanceledOnTouchOutside(true)
            dialogStart.setContentView(R.layout.task_complete)
            var taskname: TextView = dialogStart.findViewById<TextView>(R.id.taskcompleteheader)
            taskname.text=taskData.taskname
            var startdt: TextView = dialogStart.findViewById<TextView>(R.id.taskcompletestdt)
            startdt.text=taskData.startdate
            var enddt: TextView = dialogStart.findViewById<TextView>(R.id.taskcompleteendt)
            enddt.text=taskData.enddate

            var completeit: MaterialButton =
                dialogStart.findViewById<MaterialButton>(R.id.taskcompletesubmit)

            completeit.setOnClickListener {
                performUpdateOperation(taskData,position)
                dialogStart.dismiss()
            }
        }
        catch (ex:Exception)
        {
            Toast.makeText(context,"Dialog Failed to Open :${ex.message}" , Toast.LENGTH_SHORT).show()
        }

    }

}