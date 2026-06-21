package com.apexplanet.habitflow.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apexplanet.habitflow.data.model.Habit
import com.apexplanet.habitflow.data.repository.HabitRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HabitViewModel(private val repository: HabitRepository) : ViewModel() {

    val allHabits: StateFlow<List<Habit>> = repository.allHabits
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addHabit(name: String, description: String, frequency: String) {
        viewModelScope.launch {
            repository.insert(Habit(name = name, description = description, frequency = frequency))
        }
    }

    fun toggleHabitCompletion(habit: Habit) {
        viewModelScope.launch {
            val updatedHabit = habit.copy(
                isCompletedToday = !habit.isCompletedToday,
                streakCount = if (!habit.isCompletedToday) habit.streakCount + 1 else habit.streakCount - 1
            )
            repository.update(updatedHabit)
        }
    }

    fun deleteHabit(habit: Habit) {
        viewModelScope.launch {
            repository.delete(habit)
        }
    }
}
