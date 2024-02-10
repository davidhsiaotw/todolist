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
    var title: String,
    var description: String,
    @ColumnInfo(name = "create_date") val createDate: String,
    @ColumnInfo(name = "due_date") var dueDate: String,
    var location: String,
    @ColumnInfo(name = "complete") var isCompleted: Boolean = false,
) : Parcelable