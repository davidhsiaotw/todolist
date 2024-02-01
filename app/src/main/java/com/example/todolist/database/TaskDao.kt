package com.example.todolist.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.todolist.model.Task
import kotlinx.coroutines.flow.Flow

/**
 * [decide whether to add ```suspend``` to a function](https://developer.android.com/training/data-storage/room/async-queries#options)
 */
@Dao
interface TaskDao {
    /**
     * get all to-do tasks in descending order by due date
     */
    @Query("SELECT * FROM task ORDER BY due_date DESC")
    fun getAll(): Flow<List<Task>>

    @Query("SELECT * FROM task WHERE id = :id")
    suspend fun getTask(id: Int): Task

    @Insert
    suspend fun insert(task: Task)

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)
}