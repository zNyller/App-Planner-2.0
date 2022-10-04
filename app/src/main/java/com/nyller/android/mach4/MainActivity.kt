package com.nyller.android.mach4

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.nyller.android.mach4.adapter.HabitAdapter
import com.nyller.android.mach4.constants.Constants.EXTRA_NEW_HABIT
import com.nyller.android.mach4.databinding.ActivityMainBinding
import com.nyller.android.mach4.fragments.HomeFragment
import com.nyller.android.mach4.fragments.NewHabitFragment
import com.nyller.android.mach4.fragments.PreferencesFragment
import com.nyller.android.mach4.model.Habit
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    override fun onStart() {
        super.onStart()

        binding.btnConfig.setOnClickListener { openPreferences() }

    }

    private fun openPreferences() {

        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace<PreferencesFragment>(binding.fragmentContainerView.id)
            addToBackStack(null)
        }

    }



}