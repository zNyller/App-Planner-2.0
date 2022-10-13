package com.nyller.android.mach4.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "habit_table")
data class Habit(
    @ColumnInfo(name = "habit_name") var name: String = "null",
    @ColumnInfo(name = "habit_turn") var turn: String ?= "null",
    @ColumnInfo(name = "habit_category") var category: String ?= "null",
    @ColumnInfo(name = "habit_state") var done: Boolean = false
) : Serializable {
    @PrimaryKey(autoGenerate = true)
    var uid : Int = 0
}
