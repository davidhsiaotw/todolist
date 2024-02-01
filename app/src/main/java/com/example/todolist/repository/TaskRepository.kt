package com.example.todolist.repository

import com.example.todolist.model.Task
import com.example.todolist.database.TaskDao
import kotlinx.coroutines.flow.Flow


class TaskRepository(private val taskDao: TaskDao) : ITaskRepository<Task> {
    override fun getAllTasks(): Flow<List<Task>> = taskDao.getAll()

    override suspend fun getTaskById(id: Int): Task = taskDao.getTask(id)

    override suspend fun delete(task: Task) = taskDao.delete(task)

    override suspend fun update(task: Task) = taskDao.update(task)

    override suspend fun insert(task: Task) = taskDao.insert(task)
}