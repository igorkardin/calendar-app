package com.igor.calendar.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.igor.calendar.TasksRepository
import com.igor.calendar.ui.dto.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.time.LocalDate

@ExperimentalCoroutinesApi
class MainActivityViewModel(private val tasksRepository: TasksRepository) : ViewModel() {
    val tasks
        get() = dateStateFlow.flatMapLatest { date ->
            date?.let { tasksRepository.getTasks() }
                ?: flow { emptyList<Task>() }
        }
            .flowOn(Dispatchers.IO)
            .asLiveData()

    private val dateStateFlow = MutableStateFlow<LocalDate?>(null)

    fun onDatePick(date: LocalDate) {
        dateStateFlow.value = date
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val tasksRepository: TasksRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>) = MainActivityViewModel(tasksRepository) as T
    }
}