package com.igor.calendar.ui.dto

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class Task(
    val id: Int,
    @SerializedName("date_start") val dateStart: LocalDateTime,
    @SerializedName("date_end") val dateFinished: LocalDateTime,
    val name: String,
    val description: String
)