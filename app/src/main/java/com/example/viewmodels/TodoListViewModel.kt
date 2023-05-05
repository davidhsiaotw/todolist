package com.example.viewmodels

import androidx.lifecycle.*
import com.example.database.task.Task
import com.example.database.task.TaskDao
import kotlinx.coroutines.launch

class TodoListViewModel(private val taskDao: TaskDao) : ViewModel() {
    val allTasks: LiveData<List<Task>> = taskDao.getAll().asLiveData()

    private var _selectedId = MutableLiveData<Int?>()
    var selectedId: LiveData<Int?> = _selectedId

    fun addNewTask(
        title: String, description: String, createDate: String, dueDate: String, location: String
    ) {
        val newTask = Task(
            title = title, description = description, createDate = createDate, dueDate = dueDate,
            location = location
        )
        insert(newTask)
    }

    private fun insert(task: Task) {
        viewModelScope.launch { taskDao.insert(task) }
    }

    fun updateTask(
        id: Int, title: String, description: String, createDate: String, dueDate: String,
        location: String
    ) {
        val updateTask = Task(
            id, title = title, description = description, createDate = createDate,
            dueDate = dueDate, location = location
        )
        update(updateTask)
    }

    fun completeTask(task: Task) {
        val temp = !task.isCompleted
        task.isCompleted = temp
        update(task)
    }

    private fun update(task: Task) {
        viewModelScope.launch { taskDao.update(task) }
    }

    fun delete(task: Task) {
        viewModelScope.launch { taskDao.delete(task) }
    }

    fun getTaskById(id: Int): LiveData<Task> {
        return taskDao.getTask(id).asLiveData()
    }

    fun setSelectedId(id: Int?) {
        _selectedId.value = id
    }
}
