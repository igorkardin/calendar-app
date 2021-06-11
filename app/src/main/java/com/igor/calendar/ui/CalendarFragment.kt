package com.igor.calendar.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import com.igor.calendar.App
import com.igor.calendar.R
import com.igor.calendar.databinding.CalendarFragmentBinding
import com.igor.calendar.presentation.MainActivityViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import java.time.LocalDate

@ExperimentalCoroutinesApi
class CalendarFragment : Fragment() {
    private lateinit var binding: CalendarFragmentBinding
    private val viewModel by activityViewModels<MainActivityViewModel> { (requireActivity().application as App).viewModelFactory }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = CalendarFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.calendar.setOnDateChangeListener { _, year, month, dayOfMonth ->
            viewModel.onDatePick(LocalDate.of(year, month + 1, dayOfMonth))
        }
        val adapter = TasksAdapter()
        adapter.setItemClickListener { viewModel.selectTask(it) }
        val dividerItemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL).apply {
            ContextCompat.getDrawable(requireContext(), R.drawable.task_items_divider)?.let { setDrawable(it) }
        }
        binding.tasks.addItemDecoration(dividerItemDecoration)
        binding.tasks.adapter = adapter
        lifecycleScope.launchWhenResumed { viewModel.tasks.collect { adapter.setItems(it) } }
    }
}