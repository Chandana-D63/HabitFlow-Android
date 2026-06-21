package com.apexplanet.habitflow.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "habits")
data class Habit(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val description: String,
    val frequency: String, // e.g., "Daily", "Weekly"
    val streakCount: Int = 0,
    val isCompletedToday: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
