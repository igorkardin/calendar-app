package com.igor.calendar.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.igor.calendar.data.TasksRepository
import com.igor.calendar.ui.dto.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import java.time.LocalDate

@ExperimentalCoroutinesApi
class MainActivityViewModel(private val tasksRepository: TasksRepository) : ViewModel() {
    val selectedTask: StateFlow<Task?> get() = selectedTaskFlow
    private val selectedTaskFlow = MutableStateFlow<Task?>(null)

    val tasks
        get() = dateStateFlow.flatMapLatest { tasksRepository.getTasks(it) }
            .flowOn(Dispatchers.IO)

    private val dateStateFlow = MutableStateFlow<LocalDate>(LocalDate.now())

    fun selectTask(task: Task?) {
        selectedTaskFlow.value = task
    }

    fun onDatePick(date: LocalDate) {
        dateStateFlow.value = date
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val tasksRepository: TasksRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>) = MainActivityViewModel(tasksRepository) as T
    }
}