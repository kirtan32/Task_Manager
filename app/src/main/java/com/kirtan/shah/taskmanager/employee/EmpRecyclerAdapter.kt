package com.kirtan.shah.taskmanager.employee

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.kirtan.shah.taskmanager.R
import com.kirtan.shah.taskmanager.task.TaskData

class EmpRecyclerAdapter(val clickHandler: ClickHandler) : RecyclerView.Adapter<EmpRecyclerAdapter.MyViewHolder>()
{

    var TAG="Employee Task RecyclerView"
    var taskdt = ArrayList<TaskData>()

    fun setContentList(list: ArrayList<TaskData>) {
        this.taskdt = list
        //notifyDataSetChanged()
    }
    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val task = view.findViewById<TextView>(R.id.remaintaskHeader)
        val stdt = view.findViewById<TextView>(R.id.remaincardstartdt)
        val endt=view.findViewById<TextView>(R.id.remaincardenddt)
        val isCompleted=view.findViewById<ImageView>(R.id.remaincardImageView)
        val root=view.findViewById<CardView>(R.id.empCardView)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.remaintask_list_item, parent, false)

        return  MyViewHolder(view)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.task.text = taskdt[position].taskname
        holder.stdt.text = taskdt[position].startdate
        holder.endt.text = taskdt[position].enddate
        //var isCom : Boolean = taskdt[position].completed
        holder.isCompleted.setImageResource(R.drawable.progress)
        holder.root.setOnClickListener {
            clickHandler.handleClick(taskdt[position],position)
        }
        Log.d(TAG,"---> ${taskdt[position].taskname}")
    }

    override fun getItemCount(): Int {
        Log.d(TAG,"GetCount: ${taskdt.size}")
        //Toast.makeText(this,"Recycler Count:${taskdt.size}",Toast.LENGTH_SHORT)
        return taskdt.size
    }
}