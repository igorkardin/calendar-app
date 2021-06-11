package com.igor.calendar.ui.dto

import java.time.LocalDateTime

data class Task(
    val id: Int,
    val dateStart: LocalDateTime,
    val dateFinished: LocalDateTime,
    val name: String,
    val description: String
)