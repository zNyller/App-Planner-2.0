package com.nyller.android.mach4.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NewHabitViewModel: ViewModel() {

    val name = MutableLiveData<String>()

    private var days = ""
    private var turn = ""
    private var category = ""

    fun verifyFields() {
        Log.i("Edu", "Click!")
        if (name.value?.isEmpty() == true) {
            Log.i("Edu", "Name está vazio! Preencher!")
        }
    }

}