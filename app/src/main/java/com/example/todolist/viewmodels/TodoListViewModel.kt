package com.example.todolist.viewmodels

import androidx.lifecycle.*
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.todolist.TodoListApplication
import com.example.todolist.model.Task
import com.example.todolist.repository.ITaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TodoListViewModel(private val taskRepository: ITaskRepository<Task>) : ViewModel() {
    val allTasks: LiveData<List<Task>> = taskRepository.getAllTasks().asLiveData()

    fun addNewTask(task: Task) {
        viewModelScope.launch {
            taskRepository.insert(task)
        }
    }

    fun updateTask(
        id: Int, title: String, description: String, createDate: String, dueDate: String,
        location: String
    ) {
        viewModelScope.launch {
            val updateTask = Task(
                id = id, title = title, description = description, createDate = createDate,
                dueDate = dueDate, location = location
            )
            taskRepository.update(updateTask)
        }
    }

    fun completeTask(task: Task) {
        val temp = !task.isCompleted
        task.isCompleted = temp
        //update(task)
    }

    fun delete(task: Task) {
        viewModelScope.launch(context = Dispatchers.IO) { taskRepository.delete(task) }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as TodoListApplication)
                TodoListViewModel(application.container.taskRepository)
            }
        }
    }
}
