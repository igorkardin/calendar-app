package com.igor.calendar.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.igor.calendar.App
import com.igor.calendar.R
import com.igor.calendar.databinding.MainActivityBinding
import com.igor.calendar.presentation.MainActivityViewModel
import com.igor.calendar.ui.dto.Task
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect

@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity() {
    private val binding: MainActivityBinding by lazy { MainActivityBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<MainActivityViewModel> { (application as App).viewModelFactory }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        lifecycleScope.launchWhenResumed { viewModel.selectedTask.collect { navigate(it) } }
    }

    private fun navigate(task: Task?) {
        val fragment = task?.let { DetailsFragment() } ?: run { CalendarFragment() }
        binding.toolbar.setNavigationOnClickListener { onBackPressed() }
        binding.toolbar.navigationIcon = task?.let { ContextCompat.getDrawable(this, R.drawable.ic_baseline_arrow_back_24) }
        val previousFragment = supportFragmentManager.findFragmentById(binding.fragmentContainer.id)
        supportFragmentManager.apply {
            beginTransaction().apply {
                previousFragment?.let { replace(binding.fragmentContainer.id, fragment) }
                    ?: run { add(binding.fragmentContainer.id, fragment) }
            }.commit()
        }
    }


    override fun onBackPressed() {
        viewModel.selectedTask.value?.let { viewModel.selectTask(null) } ?: run { super.onBackPressed() }
    }
}