package com.nyller.android.mach4.repositories

import androidx.annotation.WorkerThread
import com.nyller.android.mach4.database.daos.habitDAO
import com.nyller.android.mach4.database.models.Habit
import kotlinx.coroutines.flow.Flow

class HabitsRepository(private val habitDAO: habitDAO) {

    val allHabits : Flow<List<Habit>> = habitDAO.getHabits()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(habit: Habit) {
        habitDAO.insert(habit)
    }

}