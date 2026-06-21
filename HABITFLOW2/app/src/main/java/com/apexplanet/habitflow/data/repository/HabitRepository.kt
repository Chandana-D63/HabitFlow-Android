package com.apexplanet.habitflow.data.repository

import com.apexplanet.habitflow.data.local.HabitDao
import com.apexplanet.habitflow.data.model.Habit
import kotlinx.coroutines.flow.Flow

class HabitRepository(private val habitDao: HabitDao) {
    val allHabits: Flow<List<Habit>> = habitDao.getAllHabits()

    suspend fun insert(habit: Habit) {
        habitDao.insertHabit(habit)
    }

    suspend fun update(habit: Habit) {
        habitDao.updateHabit(habit)
    }

    suspend fun delete(habit: Habit) {
        habitDao.deleteHabit(habit)
    }

    suspend fun getHabitById(id: Int): Habit? {
        return habitDao.getHabitById(id)
    }
}
