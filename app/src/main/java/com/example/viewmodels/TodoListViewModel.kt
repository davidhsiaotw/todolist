package com.example.viewmodels

import androidx.lifecycle.ViewModel
import com.example.database.task.Task
import com.example.database.task.TaskDao
import kotlinx.coroutines.flow.Flow

class TodoListViewModel(private val taskDao: TaskDao) : ViewModel() {
    fun fullTask(): Flow<List<Task>>  = taskDao.getAll()

}
