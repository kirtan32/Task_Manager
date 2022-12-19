package com.kirtan.shah.taskmanager.task

class TaskData
{
    lateinit var taskname:String
    lateinit var startdate:String
    lateinit var enddate:String
    var completed : Boolean = false
    lateinit var taskid : String

    //Default constructor required for calls to
    //DataSnapshot.getValue(User.class)

    constructor(){

    }

    constructor(taskid : String,name:String,sdt:String,edt:String){
        this.taskid = taskid
        this.taskname=name
        this.startdate=sdt
        this.enddate=edt
        completed=false
    }

    constructor(taskid : String,name:String,sdt:String,edt:String,isC:Boolean){
        this.taskid = taskid
        this.taskname=name
        this.startdate=sdt
        this.enddate=edt
        completed=isC
    }

}