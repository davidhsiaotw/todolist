package com.example.todolist.ui.viewholder

import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.R
import com.example.todolist.model.Task

class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val inputView = view
    private val checkBox: CheckBox = view.findViewById(R.id.checkbox)
    private val title: TextView = view.findViewById(R.id.title)
    private val description: TextView = view.findViewById(R.id.description)
    private val date: TextView = view.findViewById(R.id.date)
    private val location: TextView = view.findViewById(R.id.location)

    fun bind(onChecked: (task: Task) -> Unit, task: Task) {
        if (task.title.length > 20) {
            val subtitle = "${task.title.substring(0, 21)} ..."
            title.text = subtitle
        } else
            title.text = task.title

        val descriptionLine = task.description.split('\n')
        val firstLine = descriptionLine[0]
        if (firstLine.length > 20) {
            val subDescription = "${firstLine.substring(0, 21)} ..."
            description.text = subDescription

        } else if (descriptionLine.size > 1) {
            val subDescription = "$firstLine ..."
            description.text = subDescription

        } else {
            description.text = firstLine
        }

        date.text = inputView.context.getString(R.string.date, task.createDate, task.dueDate)
        location.text = task.location
        checkBox.isChecked = task.isCompleted
        checkBox.setOnClickListener { cb ->
            task.isCompleted = (cb as CheckBox).isChecked
            onChecked(task)
        }
    }
}