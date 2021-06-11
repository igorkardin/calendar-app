package com.igor.calendar.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import com.igor.calendar.App
import com.igor.calendar.R
import com.igor.calendar.databinding.MainActivityBinding
import com.igor.calendar.presentation.MainActivityViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.time.LocalDate

@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity() {
    private val binding: MainActivityBinding by lazy { MainActivityBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<MainActivityViewModel> { (application as App).viewModelFactory }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.calendar.setOnDateChangeListener { _, year, month, dayOfMonth ->
            viewModel.onDatePick(LocalDate.of(year, month, dayOfMonth))
        }
        val adapter = TasksAdapter()
        val dividerItemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL).apply {
            ContextCompat.getDrawable(this@MainActivity, R.drawable.task_items_divider)?.let { setDrawable(it) }
        }
        binding.tasks.addItemDecoration(dividerItemDecoration)
        binding.tasks.adapter = adapter
        viewModel.tasks.observe(this) { tasks ->
            adapter.setItems(tasks)
        }
    }
}