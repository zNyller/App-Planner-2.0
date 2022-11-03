package com.nyller.android.mach4.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nyller.android.mach4.database.models.Habit
import com.nyller.android.mach4.database.repositories.HabitsRepository
import kotlinx.coroutines.launch

class NewHabitViewModel(private val repository: HabitsRepository) : ViewModel() {

    val name = MutableLiveData<String>()

    fun delete(habit: Habit) = viewModelScope.launch {
        repository.delete(habit)
    }

    fun update(habit: Habit) = viewModelScope.launch {
        repository.update(habit)
    }

    class NewHabitViewModelFactory(private val repository: HabitsRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(NewHabitViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return NewHabitViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}