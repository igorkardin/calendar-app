package com.igor.calendar.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.igor.calendar.R
import com.igor.calendar.databinding.TaskItemBinding
import com.igor.calendar.ui.dto.Task
import java.time.LocalTime
import java.time.format.DateTimeFormatter

private const val ITEMS_COUNT = 24
private const val TIME_PATTERN = "HH:mm"

class TasksAdapter : RecyclerView.Adapter<TasksAdapter.TaskViewHolder>() {
    private var itemClickListener: ((Task) -> Unit)? = null
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
        holder.currentItem = tasks.find { it.dateStart.hour == holder.adapterPosition }
    }

    override fun getItemCount() = ITEMS_COUNT

    fun setItems(items: List<Task>) {
        tasks.clear()
        tasks.addAll(items)
        notifyDataSetChanged()
    }

    fun setItemClickListener(block: (Task) -> Unit) {
        itemClickListener = block
    }

    class TaskViewHolder(private val binding: TaskItemBinding) : RecyclerView.ViewHolder(binding.root) {
        private val timeFormatter = DateTimeFormatter.ofPattern(TIME_PATTERN)
        var currentItem: Task? = null
            set(value) = binding.run {
                timeCell.text = LocalTime.of(adapterPosition, 0).format(timeFormatter)
                taskText.text = value?.let {
                    binding.root.context.getString(R.string.task_item_name, it.name, it.dateStart.format(timeFormatter))
                } ?: ""
                field = value
            }

    }
}