package com.example.viewmodels

import androidx.lifecycle.*
import com.example.database.task.Task
import com.example.database.task.TaskDao
import com.example.network.QuoteApi
import kotlinx.coroutines.launch

enum class QuoteApiStatus { LOADING, ERROR, DONE }

class TodoListViewModel(private val taskDao: TaskDao) : ViewModel() {
    val allTasks: LiveData<List<Task>> = taskDao.getAll().asLiveData()

    private var _selectedId = MutableLiveData<Int?>()
    var selectedId: LiveData<Int?> = _selectedId

    private val _status = MutableLiveData<QuoteApiStatus>()
    val status: LiveData<QuoteApiStatus> = _status

    private val _quote = MutableLiveData<String>()
    val quote: LiveData<String> = _quote

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

    fun getQuote(minLength: Int, maxLength: Int) {
        viewModelScope.launch {
            _status.value = QuoteApiStatus.LOADING
            try {
                val result = QuoteApi.retrofitService.getQuote(maxLength, minLength)
                _quote.value = result[0]["content"] as String
                _status.value = QuoteApiStatus.DONE
            } catch (exception: Exception) {
                print("==========$exception==========")
                _quote.value = ""
                _status.value = QuoteApiStatus.ERROR
            }
        }
    }

}
