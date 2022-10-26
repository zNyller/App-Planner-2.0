package com.nyller.android.mach4.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.nyller.android.mach4.R
import com.nyller.android.mach4.application.MyApplication
import com.nyller.android.mach4.database.models.Habit
import com.nyller.android.mach4.databinding.ActivityEditHabitBinding
import com.nyller.android.mach4.ui.viewmodel.NewHabitViewModel

class EditHabitActivity : AppCompatActivity() {

    private lateinit var binding : ActivityEditHabitBinding

    private val mNewHabitViewModel: NewHabitViewModel by viewModels {
        NewHabitViewModel.NewHabitViewModelFactory((application as MyApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditHabitBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val habitReceived = intent.getSerializableExtra("HABIT_DETAILS") as Habit

        binding.edtHabitName.hint = habitReceived.name

        binding.btnDeleteHabit.setOnClickListener {

            val dialog = AlertDialog.Builder(this)
            dialog.apply {
                setTitle("Deletar hábito")
                setMessage("Tem certeza que deseja excluir este Hábito?")
                setPositiveButton("Excluir") {_, _ ->
                    mNewHabitViewModel.delete(habitReceived)
                    Toast.makeText(this@EditHabitActivity, "Hábito excluído!", Toast.LENGTH_SHORT).show()
                    finish()
                }
                setNegativeButton("Cancelar") {dialog, _ ->
                    dialog.dismiss()
                }
            }
            dialog.show()

        }

        binding.btnSalvar.setOnClickListener {
            mNewHabitViewModel.update(habitReceived)
            Toast.makeText(this, "Atualizado!", Toast.LENGTH_SHORT).show()
            finish()
        }

    }
}