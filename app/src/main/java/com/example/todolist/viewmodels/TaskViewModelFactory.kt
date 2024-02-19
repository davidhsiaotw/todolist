package com.example.todolist.viewmodels

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.example.todolist.TodoListApplication
import com.example.todolist.repository.ITaskRepository

/**
 * Factory for creating a [TaskViewModel] with a constructor that takes a [SavedStateHandle] and an [ITaskRepository].
 * @see <a href="https://github.com/yatw/saveStateHandleDemo/blob/e9ae751346039c82ab454a46bcc65ee70791739c/app/src/main/java/com/example/savestatehandledemo/MyViewModel.kt#L9">
 *     Example of ViewModel taking Application as a parameter</a>
 */
class TaskViewModelFactory(
    private val application: TodoListApplication,
    owner: SavedStateRegistryOwner, defaultArgs: Bundle? = null
) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        Log.d("TaskViewModelFactory", key)
        // reference: CreationExtras 是怎么解决的？ in https://blog.csdn.net/vitaviva/article/details/123321254
        return when (modelClass) {
            TaskViewModel::class.java -> {
                TaskViewModel(handle, application.container.taskRepository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}