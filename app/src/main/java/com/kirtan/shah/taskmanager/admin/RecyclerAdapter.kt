package com.kirtan.shah.taskmanager.admin

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.kirtan.shah.taskmanager.R
import com.kirtan.shah.taskmanager.task.TaskData

class RecyclerAdapter() : RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>()
{
    var TAG="Admin Task RecyclerView"
    var taskdt = ArrayList<TaskData>()
    //lateinit var context : Context

    //@SuppressLint("NotifyDataSetChanged")
    fun setContentList(list: ArrayList<TaskData>) {
        this.taskdt = list
        //notifyDataSetChanged()
    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val task = view.findViewById<TextView>(R.id.taskHeader)
        val stdt = view.findViewById<TextView>(R.id.cardstartdt)
        val endt=view.findViewById<TextView>(R.id.cardenddt)
        val isCompleted=view.findViewById<ImageView>(R.id.cardImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)

        return  MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.task.text = taskdt[position].taskname
        holder.stdt.text = taskdt[position].startdate
        holder.endt.text = taskdt[position].enddate
        var isCom : Boolean = taskdt[position].completed
        if(isCom)
            holder.isCompleted.setImageResource(R.drawable.complete)
        else
            holder.isCompleted.setImageResource(R.drawable.progress)
        Log.d(TAG,"---> ${taskdt[position].taskname}")
    }

    override fun getItemCount(): Int {
        Log.d(TAG,"GetCount: ${taskdt.size}")
        //Toast.makeText(this,"Recycler Count:${taskdt.size}",Toast.LENGTH_SHORT)
        return taskdt.size
    }

}