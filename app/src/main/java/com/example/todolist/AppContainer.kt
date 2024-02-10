package com.example.todolist

import android.content.Context
import com.example.todolist.database.AppDatabase
import com.example.todolist.model.Task
import com.example.todolist.repository.ITaskRepository
import com.example.todolist.repository.TaskRepository

class AppContainer(context: Context) {
    val taskDatabase: AppDatabase by lazy { AppDatabase.getDatabase(context) }
    val taskRepository: ITaskRepository<Task> = TaskRepository(taskDatabase.taskDao())
}