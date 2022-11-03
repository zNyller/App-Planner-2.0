package com.nyller.android.mach4.ui.activities

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.nyller.android.mach4.application.MyApplication
import com.nyller.android.mach4.database.models.Habit
import com.nyller.android.mach4.databinding.ActivityEditHabitBinding
import com.nyller.android.mach4.ui.viewmodel.NewHabitViewModel

class EditHabitActivity : AppCompatActivity() {

    private lateinit var binding : ActivityEditHabitBinding

    private val mNewHabitViewModel: NewHabitViewModel by viewModels {
        NewHabitViewModel.NewHabitViewModelFactory((application as MyApplication).repository)
    }

    private var turn = ""
    private var category = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditHabitBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val habitReceived = intent.getSerializableExtra("HABIT_DETAILS") as Habit

        Log.i("Edu", "Habito recebido: $habitReceived")

        binding.edtHabitName.setText(habitReceived.name)

        binding.btnCategoria.setOnClickListener {

            val options = arrayOf("Alimentação", "Bem Estar", "Criatividade", "Foco", "Organização")

            val dialog = this.let { AlertDialog.Builder(it) }
            dialog.apply {
                setTitle("Selecione uma categoria:")
                setSingleChoiceItems(options, -1) { _, selectedOption ->
                    category = selectedOption.toString()
                }
                setPositiveButton("Confirmar") { _, _ ->

                    if (category == "-1") {
                        Toast.makeText(context, "Selecione uma categoria!", Toast.LENGTH_LONG).show()
                        return@setPositiveButton
                    }
                    Toast.makeText(context, "Feito!", Toast.LENGTH_SHORT).show()
                }
            }
            dialog.show()
        }

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
            if (category == "0") category = "Alimentação"
            if (category == "1") category = "Bem Estar"
            if (category == "2") category = "Criatividade"
            if (category == "3") category = "Foco"
            if (category == "4") category = "Organização"

            val selectedTurn = binding.rgTurnos.checkedRadioButtonId

            if (selectedTurn == binding.rbManha.id) turn = "Manhã"
            if (selectedTurn == binding.rbTarde.id) turn = "Tarde"
            if (selectedTurn == binding.rbNoite.id) turn = "Noite"
            if (selectedTurn == binding.rbQualquerHora.id) turn = "Qualquer hora"

            if (binding.edtHabitName.text.isEmpty()) {
                binding.edtHabitName.error = "Por favor, preencha o nome do seu novo Hábito!"
                binding.edtHabitName.requestFocus()
                return@setOnClickListener
            }

            if (category.isEmpty()) {
                Toast.makeText(this, "Selecione uma categoria!", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (turn.isEmpty()) {
                binding.rgTurnos.requestFocus()
                Toast.makeText(this, "Selecione algum turno!", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            habitReceived.name = binding.edtHabitName.text.toString()
            habitReceived.turn = turn
            habitReceived.category = category

            mNewHabitViewModel.update(habitReceived)
            finish()
        }
    }
}