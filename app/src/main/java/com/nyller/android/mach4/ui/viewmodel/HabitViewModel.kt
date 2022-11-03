package com.nyller.android.mach4.ui.viewmodel

import androidx.lifecycle.*
import com.nyller.android.mach4.database.models.Habit
import com.nyller.android.mach4.database.repositories.HabitsRepository
import kotlinx.coroutines.launch

class HabitViewModel(private val repository: HabitsRepository) : ViewModel() {

    val allHabits: LiveData<List<Habit>> = repository.allHabits.asLiveData()

    private val _openHabitDetail = MutableLiveData<Habit?>()
    val openHabitDetail
        get() = _openHabitDetail

    fun onHabitClicked(id: Habit) {
        _openHabitDetail.value = id
    }

    // Após abrir os detalhes:
    fun onHabitOpened() {
        _openHabitDetail.value = null
    }

    /* Launching a new coroutine to insert the data in a non-blocking way */
    fun insert(habit: Habit) = viewModelScope.launch {
        repository.insert(habit)
    }

    private fun getUpdatedHabitEntry(
        habitId: Long,
        habitName: String,
        habitTurn: String,
        habitCategory: String
    ): Habit {
        return Habit(
            habitId = habitId,
            name = habitName,
            turn = habitTurn,
            category = habitCategory
        )
    }

    fun updateItem(
        habitId: Long,
        habitName: String,
        habitTurn: String,
        habitCategory: String
    ) {
        val updatedItem = getUpdatedHabitEntry(habitId, habitName, habitTurn, habitCategory)
        updateItem(updatedItem)
    }

    private fun updateItem(habit: Habit) = viewModelScope.launch {
        repository.update(habit)
    }

    // Ao usar viewModels e ViewModelProvider.Factory, o framework cuidará do ciclo de vida do ViewModel.
    class HabitViewModelFactory(private val repository: HabitsRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HabitViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return HabitViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}