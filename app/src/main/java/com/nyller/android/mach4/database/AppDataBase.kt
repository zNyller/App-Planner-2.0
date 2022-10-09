package com.nyller.android.mach4.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.nyller.android.mach4.database.daos.habitDAO
import com.nyller.android.mach4.model.Habit

@Database(entities = [Habit::class], version = 1)
abstract class AppDataBase : RoomDatabase() {

    abstract fun habitDao(): habitDAO

    companion object {

        private const val DATABASE_NAME = "banco-de-dados"

        @Volatile
        private var INSTANCE: AppDataBase? = null

        fun getInstance(context: Context): AppDataBase =
            INSTANCE?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDataBase::class.java, DATABASE_NAME
            ).build()

    }

}