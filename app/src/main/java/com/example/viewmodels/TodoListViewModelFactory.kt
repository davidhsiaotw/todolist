package com.example.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.database.task.TaskDao

@Suppress("UNCHECKED_CAST")
class TodoListViewModelFactory(private val taskDao: TaskDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass.isAssignableFrom(TodoListViewModel::class.java))
            return TodoListViewModel(taskDao) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}