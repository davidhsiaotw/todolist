package com.example.todolist

import android.app.Application
import com.example.database.AppDatabase

class TodoListApplication : Application() {
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }
}