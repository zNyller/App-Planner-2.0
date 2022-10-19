package com.nyller.android.mach4.ui.viewmodel

import androidx.lifecycle.*
import com.nyller.android.mach4.database.models.Habit
import com.nyller.android.mach4.repositories.HabitsRepository
import kotlinx.coroutines.launch

class HabitViewModel(private val repository: HabitsRepository) : ViewModel() {

    val allHabits: LiveData<List<Habit>> = repository.allHabits.asLiveData()

    /* Launching a new coroutine to insert the data in a non-blocking way */
    fun insert(habit: Habit) = viewModelScope.launch {
        repository.insert(habit)
    }

    fun delete(habit: Habit) = viewModelScope.launch {
        repository.delete(habit)
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