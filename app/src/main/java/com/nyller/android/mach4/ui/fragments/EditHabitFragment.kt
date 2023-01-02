package com.nyller.android.mach4.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.nyller.android.mach4.application.MyApplication
import com.nyller.android.mach4.databinding.FragmentEditHabitBinding
import com.nyller.android.mach4.ui.viewmodel.NewHabitViewModel

class EditHabitFragment : Fragment() {

    private val binding by lazy {
        FragmentEditHabitBinding.inflate(layoutInflater)
    }

    private val newHabitViewModel : NewHabitViewModel by viewModels {
        NewHabitViewModel.NewHabitViewModelFactory((activity?.application as MyApplication).repository)
    }

    private val args : EditHabitFragmentArgs by navArgs()

    private var category = ""
    private var turn = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding.edtHabitName.setText(args.habitEdit.name)

        binding.btnCategoria.setOnClickListener {
            openCategories()
        }

        binding.btnDeleteHabit.setOnClickListener { dialogDelete() }

        binding.btnSalvar.setOnClickListener { verifyFields() }

        return binding.root
    }

    private fun openCategories() {
        val options = arrayOf("Alimentação", "Bem Estar", "Criatividade", "Foco", "Organização")

        val dialog = context?.let { AlertDialog.Builder(it) }
        dialog?.apply {
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

        when (binding.rgTurnos.checkedRadioButtonId) {
            binding.rbQualquerHora.id -> turn = "Qualquer hora"
            binding.rbManha.id -> turn = "Manhã"
            binding.rbTarde.id -> turn = "Tarde"
            binding.rbNoite.id -> turn = "Noite"
        }

        if (category.isEmpty()) {
            Toast.makeText(context, "Selecione uma categoria!", Toast.LENGTH_LONG).show()
            return
        }

        if (turn.isEmpty()) {
            Toast.makeText(context, "Selecione algum turno!", Toast.LENGTH_LONG).show()
            return
        }

        args.habitEdit.name = binding.edtHabitName.text.toString()
        args.habitEdit.category = category
        args.habitEdit.turn = turn

        newHabitViewModel.update(args.habitEdit)
        findNavController().navigateUp()

    }

    private fun dialogDelete() {

        val dialog = context?.let { AlertDialog.Builder(it) }
        dialog?.apply {
            setTitle("Deletar hábito")
            setMessage("Tem certeza que deseja deletar este hábito?")
            setPositiveButton("Excluir") { _, _ ->
                newHabitViewModel.delete(args.habitEdit)
                Toast.makeText(context, "Hábito excluído!", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            }
            setNegativeButton("Cancelar") { alert, _ ->
                alert.dismiss()
            }
        }
        dialog?.show()
    }

}