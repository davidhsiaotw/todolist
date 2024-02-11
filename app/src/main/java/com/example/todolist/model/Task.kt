package com.example.todolist.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    @ColumnInfo(name = "create_date") val createDate: String,
    @ColumnInfo(name = "due_date") val dueDate: String,
    val location: String,
    @ColumnInfo(name = "complete") val isCompleted: Boolean = false,
) : Parcelable