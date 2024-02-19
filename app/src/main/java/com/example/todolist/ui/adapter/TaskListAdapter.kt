package com.example.todolist.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.todolist.R
import com.example.todolist.model.Task
import com.example.todolist.ui.fragment.TaskAction
import com.example.todolist.ui.viewholder.TaskViewHolder

class TaskListAdapter(
    private val isVisible: Boolean, private val onItemInteraction: (TaskAction) -> Unit,
) : ListAdapter<Task, TaskViewHolder>(DiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.task_view, parent, false)
        )
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = getItem(position)
        if (task.isCompleted && !isVisible) {
            holder.itemView.visibility = View.GONE
            holder.itemView.layoutParams.height = 0
        }
        onItemInteraction(TaskAction.HideIncompleteTask(task, holder.itemView))
        holder.itemView.setOnClickListener {
            onItemInteraction(TaskAction.OnItemClicked(task))
        }
        holder.bind({ onItemInteraction(TaskAction.OnItemChecked(it)) }, task)
    }

    fun deleteTask(position: Int) {
        val task = getItem(position)
        onItemInteraction(TaskAction.OnItemSwiped(task))
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