package com.nyller.android.mach4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.nyller.android.mach4.databinding.ActivityNewHabitBinding

class NewHabitActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewHabitBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewHabitBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}