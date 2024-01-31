package com.example.todolist.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.todolist.model.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    /**
     * get all to-do tasks in descending order by due date
     */
    @Query("SELECT * FROM task ORDER BY due_date DESC")
    fun getAll(): Flow<List<Task>>

    @Query("SELECT * FROM task WHERE id = :id")
    fun getTask(id: Int): Flow<Task>

    @Insert
    suspend fun insert(task: Task)

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)
}