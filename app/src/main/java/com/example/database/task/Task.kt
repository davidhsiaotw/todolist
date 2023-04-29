package com.example.database.task

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Task(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @NonNull var title: String,
    var description: String,
    @ColumnInfo(name = "create_date") val createDate: String,
    @ColumnInfo(name = "due_date") var dueDate: String,
    var location: String
)