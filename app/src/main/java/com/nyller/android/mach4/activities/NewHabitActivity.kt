package com.nyller.android.mach4.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.nyller.android.mach4.database.models.Habit
import com.nyller.android.mach4.databinding.ActivityNewHabitBinding

class NewHabitActivity : AppCompatActivity() {

    private var days = ""
    private var turn = ""
    private var category = ""

    private val binding: ActivityNewHabitBinding by lazy {
        ActivityNewHabitBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

    }

    override fun onStart() {
        super.onStart()

        binding.btnCategoria.setOnClickListener { showCategoryDialog() }
        binding.btnCriar.setOnClickListener { verifyFields() }

    }

    private fun showCategoryDialog() {

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

    private fun verifyFields() {

        if (category == "0") category = "Alimentação"
        if (category == "1") category = "Bem Estar"
        if (category == "2") category = "Criatividade"
        if (category == "3") category = "Foco"
        if (category == "4") category = "Organização"

        if (binding.cbDomingo.isChecked) days += 0
        if (binding.cbSegunda.isChecked) days += 1
        if (binding.cbTerca.isChecked) days += 2
        if (binding.cbQuarta.isChecked) days += 3
        if (binding.cbQuinta.isChecked) days += 4
        if (binding.cbSexta.isChecked) days += 5
        if (binding.cbSabado.isChecked) days += 6

        val selectedTurn = binding.rgTurnos.checkedRadioButtonId

        if (selectedTurn == binding.rbManha.id) turn = "Manhã"
        if (selectedTurn == binding.rbTarde.id) turn = "Tarde"
        if (selectedTurn == binding.rbNoite.id) turn = "Noite"
        if (selectedTurn == binding.rbQualquerHora.id) turn = "Qualquer hora"

        if (binding.edtHabitName.text.isEmpty()) {
            binding.edtHabitName.error = "Por favor, preencha o nome do seu novo Hábito!"
            binding.edtHabitName.requestFocus()
            return
        }

        if (category.isEmpty()) {
            Toast.makeText(this, "Selecione uma categoria!", Toast.LENGTH_LONG).show()
            return
        }

        if (days.isEmpty()) {
            Toast.makeText(this, "Selecione algum dia da semana!", Toast.LENGTH_LONG).show()
            return
        }

        if (turn.isEmpty()) {
            binding.rgTurnos.requestFocus()
            Toast.makeText(this, "Selecione algum turno!", Toast.LENGTH_LONG).show()
            return
        }

        saveNewHabit()
        finish()

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