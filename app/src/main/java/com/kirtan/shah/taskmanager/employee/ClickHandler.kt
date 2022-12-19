package com.kirtan.shah.taskmanager.employee

import com.kirtan.shah.taskmanager.task.TaskData

interface ClickHandler
{
    fun handleClick(taskData: TaskData, position:Int)
}