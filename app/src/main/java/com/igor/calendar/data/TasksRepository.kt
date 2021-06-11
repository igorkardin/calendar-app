package com.igor.calendar.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import com.igor.calendar.ui.dto.Task
import kotlinx.coroutines.flow.flow
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

private const val JSON_FILENAME = "initial.json"

class TasksRepository(context: Context) {
    private val gson = GsonBuilder()
        .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
        .create()
    private val data = gson.fromAsset<Array<Task>>(context, JSON_FILENAME).toList()

    fun getTasks(date: LocalDate) = flow {
        data
            .filter { it.dateStart.toLocalDate() == date }
            .also { emit(it) }
    }

    private inline fun <reified T> Gson.fromAsset(context: Context, asset: String): T {
        context.assets.open(asset).use { stream ->
            val string = String(stream.readBytes())
            return fromJson(string, T::class.java)
        }
    }
}

class LocalDateTimeAdapter : TypeAdapter<LocalDateTime?>() {
    override fun write(jsonWriter: JsonWriter, time: LocalDateTime?) {
        time ?: return
        time.atZone(ZoneId.systemDefault()).toEpochSecond()
        jsonWriter.value(time.atZone(ZoneId.systemDefault()).toInstant().epochSecond)
    }

    override fun read(jsonReader: JsonReader): LocalDateTime {
        return LocalDateTime.ofEpochSecond(jsonReader.nextLong(), 0, ZoneOffset.UTC)
    }
}
