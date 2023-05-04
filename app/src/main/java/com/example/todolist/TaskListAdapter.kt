package com.example.todolist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.database.task.Task
import com.example.todolist.databinding.TaskViewBinding

class TaskListAdapter(
    private val onItemClicked: (Task) -> Unit, private val checkedListener: TaskCompleteListener
) : ListAdapter<Task, TaskListAdapter.TaskViewHolder>(DiffCallback) {

    class TaskViewHolder(private var binding: TaskViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(checkedListener: TaskCompleteListener, task: Task) {
            binding.apply {
                setTask(task)
                title.text = task.title
                if (task.description.length > 25) {
                    val subDescription = task.description.substring(0, 26) + "..."
                    description.text = subDescription
                } else
                    description.text = task.description
                textView.text =
                    root.context.getString(R.string.date).format(task.createDate, task.dueDate)
                location.text = task.location
                checkbox.isChecked = task.isCompleted
                setCheckedListener(checkedListener)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(
            TaskViewBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = getItem(position)
        holder.itemView.setOnClickListener {
            onItemClicked(task)
        }
        holder.bind(checkedListener, task)
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

class TaskCompleteListener(val checkedListener: (task: Task) -> Unit) {
    fun onCheck(task: Task) = checkedListener(task)
}