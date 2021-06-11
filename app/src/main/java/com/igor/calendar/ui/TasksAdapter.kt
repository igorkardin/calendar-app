package com.igor.calendar.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.igor.calendar.databinding.TaskItemBinding
import com.igor.calendar.ui.dto.Task
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlin.properties.Delegates

private const val ITEMS_COUNT = 24
private const val TIME_PATTERN = "HH:mm"

class TasksAdapter : RecyclerView.Adapter<TasksAdapter.TaskViewHolder>() {
    private val itemClickListener: ((Task) -> Unit)? = null
    private val tasks = mutableListOf<Task>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = TaskItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding).also { holder ->
            binding.root.setOnClickListener {
                val position = holder.adapterPosition
                if (position != RecyclerView.NO_POSITION) holder.currentItem?.let { item -> itemClickListener?.invoke(item) }
            }
        }
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.currentItem = null
    }

    fun setItems(items: List<Task>) {
        tasks.clear()
        tasks.addAll(items)
        notifyDataSetChanged()
    }

    override fun getItemCount() = 24

    class TaskViewHolder(private val binding: TaskItemBinding) : RecyclerView.ViewHolder(binding.root) {
        private val timeFormatter = DateTimeFormatter.ofPattern(TIME_PATTERN)

        var currentItem: Task? by Delegates.observable(null) { _, _, value ->
            binding.timeCell.text = LocalTime.of(adapterPosition, 0).format(timeFormatter)
        }
    }
}