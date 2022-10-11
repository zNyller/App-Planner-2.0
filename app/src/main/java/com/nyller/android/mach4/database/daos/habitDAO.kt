package com.nyller.android.mach4.database.daos

import androidx.room.*
import com.nyller.android.mach4.database.models.Habit
import kotlinx.coroutines.flow.Flow

@Dao
interface habitDAO {

    @Query("SELECT * FROM habit_table")  // Recupera todos hábitos
    fun getHabits(): List<Habit>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(habit: Habit)

    @Update
    suspend fun update(vararg habit: Habit)

    @Delete
    suspend fun delete(habit: Habit)

}