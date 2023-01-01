package com.nyller.android.mach4.ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.nyller.android.mach4.R
import com.nyller.android.mach4.database.models.Habit
import com.nyller.android.mach4.databinding.FragmentNewHabitBinding

class NewHabitFragment : Fragment(R.layout.fragment_new_habit) {

    private lateinit var binding: FragmentNewHabitBinding

    var category = ""
    var turn = ""

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

        binding.btnCategoria.setOnClickListener {
            showCategoryDialog()
        }

        binding.btnCriar.setOnClickListener {
            verifyFields()
        }

    }

    private fun showCategoryDialog() {
        val options = arrayOf("Alimentação", "Bem Estar", "Criatividade", "Foco", "Organização")

        val dialog = AlertDialog.Builder(context)
        dialog.apply {
            setTitle("Selecione uma categoria:")
            setSingleChoiceItems(options, -1) {_, selectedOption ->
                category = selectedOption.toString()
            }
            setPositiveButton("Confirmar") {_, _ ->
                if (category.isEmpty()) {
                    Toast.makeText(context, "Selecione uma categoria!", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }
            }
            dialog.show()
        }
    }

    private fun verifyFields() {

        if (binding.edtHabitName.text.isEmpty()) {
            binding.edtHabitName.error = "Por favor, preencha o nome do seu novo Hábito!"
            binding.edtHabitName.requestFocus()
            return
        }

        when (category) {
            "0" -> category = "Alimentação"
            "1" -> category = "Bem Estar"
            "2" -> category = "Criatividade"
            "3" -> category = "Foco"
            "4" -> category = "Organização"
        }

        if (category.isEmpty()) {
            Toast.makeText(context, "Selecione uma categoria!", Toast.LENGTH_LONG).show()
            return
        }

        when (binding.rgTurnos.checkedRadioButtonId) {
            binding.rbQualquerHora.id -> turn = "Qualquer hora"
            binding.rbManha.id -> turn = "Manhã"
            binding.rbTarde.id -> turn = "Tarde"
            binding.rbNoite.id -> turn = "Noite"
        }

        if (turn.isEmpty()) {
            binding.rgTurnos.requestFocus()
            Toast.makeText(context, "Selecione algum turno!", Toast.LENGTH_LONG).show()
            return
        }

        saveNewHabit()

    }

    private fun saveNewHabit() {

        val newHabit = Habit(
            name = binding.edtHabitName.text.toString(),
            turn = turn,
            category = category
        )

        val result = Bundle().apply {
            putSerializable("newHabit", newHabit)
        }
        setFragmentResult("request_key", result)

        findNavController().navigateUp()

    }

}