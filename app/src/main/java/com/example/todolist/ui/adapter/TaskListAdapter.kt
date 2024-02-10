package com.example.todolist.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.todolist.R
import com.example.todolist.model.Task
import com.example.todolist.ui.viewholder.TaskViewHolder

class TaskListAdapter(
    private val onItemClicked: (Task) -> Unit, private val onItemChecked: (task: Task) -> Unit,
    private val onItemSwiped: (Task) -> Unit
) : ListAdapter<Task, TaskViewHolder>(DiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.task_view, parent, false)
        )
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = getItem(position)
        holder.itemView.setOnClickListener {
            onItemClicked(task)
        }
        holder.bind(onItemChecked, task)
    }

    fun deleteTask(position: Int) {
        val task = getItem(position)
        onItemSwiped(task)
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Task>() {
            override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
                return oldItem == newItem
            }
        }
    }
}