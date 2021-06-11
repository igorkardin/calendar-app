package com.igor.calendar

import android.app.Application
import com.igor.calendar.presentation.MainActivityViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class App : Application() {
    // Simple dependency injection alternative, dagger and koin require a lot of boilerplate since i need just one dependency
    lateinit var viewModelFactory: MainActivityViewModel.Factory

    override fun onCreate() {
        super.onCreate()
        viewModelFactory = MainActivityViewModel.Factory(TasksRepository(this))
    }
}