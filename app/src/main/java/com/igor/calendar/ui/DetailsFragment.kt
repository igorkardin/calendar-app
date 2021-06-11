package com.igor.calendar.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.igor.calendar.App
import com.igor.calendar.R
import com.igor.calendar.databinding.DetailsFragmentBinding
import com.igor.calendar.presentation.MainActivityViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private const val TIME_PATTERN = "HH:mm"

@ExperimentalCoroutinesApi
class DetailsFragment : Fragment() {
    private lateinit var binding: DetailsFragmentBinding
    private val viewModel by activityViewModels<MainActivityViewModel> { (requireActivity().application as App).viewModelFactory }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DetailsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launchWhenResumed {
            viewModel.selectedTask.collect { task ->
                task ?: return@collect
                binding.apply {
                    taskDescriptionValue.text = task.description
                    taskName.text = task.name
                    taskDateValue.text =
                        getString(R.string.date_from_to, task.dateStart.toTimeString(), task.dateFinished.toTimeString())
                }
            }
        }
    }

}

private fun LocalDateTime.toTimeString() = format(DateTimeFormatter.ofPattern(TIME_PATTERN))
