package com.nyller.android.mach4.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.nyller.android.mach4.database.daos.HabitDAO
import com.nyller.android.mach4.database.models.Habit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Habit::class], version = 2)
abstract class AppDataBase : RoomDatabase() {

    abstract fun habitDao(): HabitDAO

    private class HabitDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { dataBase ->
                scope.launch {
                    populateDatabase(dataBase.habitDao())
                }
            }
        }

        suspend fun populateDatabase(habitDao: HabitDAO) {

            habitDao.deleteAll()

            val example = Habit( name ="Beber 2L de Água", turn = "Turno", category = "Exemplo")
            habitDao.insert(example)

        }

    }

    companion object {
        // Singleton prevents multiple instances of database opening at the same time.
        private const val DATABASE_NAME = "banco-de-dados"

        @Volatile
        private var INSTANCE: AppDataBase? = null

        fun getInstance(
            context: Context,
            scope: CoroutineScope
        ): AppDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDataBase::class.java, DATABASE_NAME
                )
                    .addCallback(HabitDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}