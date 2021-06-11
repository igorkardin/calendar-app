package com.igor.calendar

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import com.igor.calendar.ui.dto.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDateTime
import java.time.ZoneOffset

private const val JSON_FILENAME = "initial.json"
private const val NANOSECONDS = 50000

class TasksRepository(private val context: Context) {
    private val gson = GsonBuilder()
        .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
        .create()

    fun getTasks(): Flow<List<Task>> = flow {
        emit(gson.fromAsset<Array<Task>>(context, JSON_FILENAME).toList())
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
        jsonWriter.value(time?.toEpochSecond(ZoneOffset.UTC))
    }

    override fun read(jsonReader: JsonReader): LocalDateTime {
        return LocalDateTime.ofEpochSecond(jsonReader.nextLong(), NANOSECONDS, ZoneOffset.UTC)
    }
}
