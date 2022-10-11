package com.nyller.android.mach4.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.nyller.android.mach4.database.models.Habit
import com.nyller.android.mach4.repositories.HabitsRepository
import kotlinx.coroutines.launch

class HabitViewModel(private val repository: HabitsRepository) : ViewModel() {

//    val allHabits : LiveData<List<Habit>> = repository.allHabits.asLiveData()

    fun insert(habit: Habit) = viewModelScope.launch {
        repository.insert(habit)
    }

}