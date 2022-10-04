package com.nyller.android.mach4.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import com.nyller.android.mach4.databinding.FragmentNewHabitBinding
import com.nyller.android.mach4.model.Habit

class NewHabitFragment : Fragment() {

    private lateinit var binding: FragmentNewHabitBinding
    private var days = ""
    private var turn = ""
    private var category = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewHabitBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCategoria.setOnClickListener { showCategoryDialog() }
        binding.btnCriar.setOnClickListener { verifyFields() }

    }

    private fun showCategoryDialog() {

        val options = arrayOf("Alimentação", "Bem Estar", "Criatividade", "Foco", "Organização")

        val dialog = context?.let { AlertDialog.Builder(it) }
        dialog?.apply {
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

        dialog?.show()

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
            Toast.makeText(context, "Selecione uma categoria!", Toast.LENGTH_LONG).show()
            return
        }

        if (days.isEmpty()) {
            Toast.makeText(context, "Selecione algum dia da semana!", Toast.LENGTH_LONG).show()
            return
        }

        if (turn.isEmpty()) {
            binding.rgTurnos.requestFocus()
            Toast.makeText(context, "Selecione algum turno!", Toast.LENGTH_LONG).show()
            return
        }

        saveNewHabit()

    }

    private fun saveNewHabit() {

        val newHabit = bundleOf(
            "HABIT" to Habit(
                binding.edtHabitName.text.toString(),
                turn = turn,
                category = category
            )
        )

        setFragmentResult("RESULT", newHabit)
        parentFragmentManager.popBackStack()
    }
}
