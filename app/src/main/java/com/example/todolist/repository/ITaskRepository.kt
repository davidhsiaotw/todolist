package com.example.todolist.repository

import kotlinx.coroutines.flow.Flow

interface ITaskRepository<T> {
    fun getAllTasks(): Flow<List<T>>
    suspend fun getTaskById(id: Int): T
    suspend fun insert(task: T)
    suspend fun update(task: T)
    suspend fun delete(task: T)
}