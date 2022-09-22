package com.nyller.android.mach4

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.nyller.android.mach4.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.fabNewHabit.setOnClickListener {

            openNewHabit()

        }

    }

    private fun openNewHabit() {

        startActivity(Intent(this, NewHabitActivity::class.java))

    }

}