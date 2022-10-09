package com.nyller.android.mach4.database.daos

import androidx.room.*
import com.nyller.android.mach4.model.Habit

@Dao
interface habitDAO {

    @Query("SELECT * FROM habit")  // Recupera todos hábitos
    suspend fun getHabits(): List<Habit>

    @Insert
    suspend fun insert(habit: Habit)

    @Update
    suspend fun update(vararg habit: Habit)

    @Delete
    suspend fun delete(habit: Habit)

}