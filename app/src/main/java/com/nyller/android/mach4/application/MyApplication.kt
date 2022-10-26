package com.nyller.android.mach4.application

import android.app.Application
import com.nyller.android.mach4.database.AppDataBase
import com.nyller.android.mach4.database.repositories.HabitsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class MyApplication: Application() {

    val applicationScope = CoroutineScope(SupervisorJob())

    // by lazy para que o banco de dados e o repositório só sejam criados quando NECESSÁRIO ao invés de quando o app iniciar
    val database by lazy { AppDataBase.getInstance(this, applicationScope) }
    val repository by lazy { HabitsRepository(database.habitDao()) }

}