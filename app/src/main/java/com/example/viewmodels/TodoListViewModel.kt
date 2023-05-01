package com.example.viewmodels

import android.annotation.SuppressLint
import android.location.Location
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.database.task.Task
import com.example.database.task.TaskDao
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TodoListViewModel(private val taskDao: TaskDao) : ViewModel() {
    val allTasks: LiveData<List<Task>> = taskDao.getAll().asLiveData()

    fun addNewTask(
        title: String, description: String, createDate: LocalDateTime, dueDate: LocalDateTime,
        location: Location
    ) {
        val newTask = Task(
            title = title, description = description,
            createDate = createDate.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
            dueDate = dueDate.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
            location = location.toString()
        )
        insert(newTask)
    }

    private fun insert(task: Task) {
        viewModelScope.launch { taskDao.insert(task) }
    }

    fun updateTask(
        id: Int, title: String, description: String, createDate: LocalDateTime,
        dueDate: LocalDateTime, location: Location
    ) {
        val updateTask = Task(
            id, title = title, description = description,
            createDate = createDate.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
            dueDate = dueDate.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
            location = location.toString()
        )
        update(updateTask)
    }

    private fun update(task: Task) {
        viewModelScope.launch { taskDao.update(task) }
    }

    private fun delete(task: Task) {
        viewModelScope.launch { taskDao.delete(task) }
    }

}
