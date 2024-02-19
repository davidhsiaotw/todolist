package com.example.todolist.viewmodels

import androidx.lifecycle.*
import com.example.todolist.model.Task
import com.example.todolist.repository.ITaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskViewModel(
    private val handle: SavedStateHandle,
    private val taskRepository: ITaskRepository<Task>
) : ViewModel() {
    val allTasks: LiveData<List<Task>> = taskRepository.getAllTasks().asLiveData()

    fun addNewTask(task: Task) {
        viewModelScope.launch(context = Dispatchers.IO) { taskRepository.insert(task) }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch(context = Dispatchers.IO) { taskRepository.update(task) }
    }

    fun delete(task: Task) {
        viewModelScope.launch(context = Dispatchers.IO) { taskRepository.delete(task) }
    }

    fun saveVisibility(isVisible: Boolean) {
        handle["isVisible"] = isVisible
//        _isVisible.postValue(isVisible)
    }
}
