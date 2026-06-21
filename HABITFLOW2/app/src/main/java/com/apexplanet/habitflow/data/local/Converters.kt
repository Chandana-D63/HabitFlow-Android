package com.apexplanet.habitflow.data.local

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class Converters {
    @TypeConverter
    fun fromList(value: List<Long>): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun toList(value: String): List<Long> {
        return try {
            Json.decodeFromString(value)
        } catch (e: Exception) {
            emptyList()
        }
    }
}
