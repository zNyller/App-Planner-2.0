package com.nyller.android.mach4.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.nyller.android.mach4.database.models.Habit
import com.nyller.android.mach4.databinding.ActivityNewHabitBinding

class NewHabitActivity : AppCompatActivity() {

    private var turn = ""
    private var category = ""

    private val binding: ActivityNewHabitBinding by lazy {
        ActivityNewHabitBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

    }

    companion object {
        const val EXTRA_REPLY = "send data"
    }

    private fun saveNewHabit() {

        val habit = Habit(
            name = binding.edtHabitName.text.toString(),
            turn = turn,
            category = category
        )

        val replyIntent = Intent()
        replyIntent.putExtra(EXTRA_REPLY, habit)
        Log.i("Edu", "Passando os dados : \n $replyIntent, $habit")
        setResult(RESULT_OK, replyIntent)
        finish()

    }
}