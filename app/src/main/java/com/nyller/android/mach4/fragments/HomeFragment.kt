package com.nyller.android.mach4.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.nyller.android.mach4.adapter.HabitAdapter
import com.nyller.android.mach4.databinding.FragmentHomeBinding
import com.nyller.android.mach4.model.Habit
import java.util.*

class HomeFragment : Fragment() {

    private lateinit var binding : FragmentHomeBinding
    private lateinit var adapter: HabitAdapter
    private lateinit var habit : Habit

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdapter()

        swipe()
        binding.fabNewHabit.setOnClickListener { openNewHabit() }

    }

    private fun openNewHabit() {
        parentFragmentManager.commit {
            setReorderingAllowed(true)
            replace<NewHabitFragment>(binding.constraintRoot.id)
            addToBackStack(null)
        }

        setFragmentResultListener("RESULT") {_, bundle ->
            this.habit = bundle.getSerializable("HABIT") as Habit
            adapter.addHabit(this.habit)
        }

    }

    private fun setupAdapter() {

        adapter = HabitAdapter(
            onClick = { habit ->
                showHabitDetails(habit) { habitSelected ->
                    adapter.updateHabit(habitSelected)
                }
            }
        )
        binding.rvHabits.adapter = adapter
        binding.rvHabits.setHasFixedSize(true)

    }

    private fun showHabitDetails(habit: Habit, onHabitStatusChanged: (Habit) -> Unit) {

        val dialog = context?.let { AlertDialog.Builder(it) }
        dialog?.apply {
            setTitle("Detalhes do Hábito")
            setMessage(
                """"
                    Nome: ${habit.name}
                    Categoria: ${habit.category}
                    Turno: ${habit.turn}
                """.trimMargin()
            )
            setPositiveButton(if (habit.done) "Concluído" else "Concluir") { _, _ ->
                habit.done = !habit.done
                onHabitStatusChanged(habit)
            }
            setNegativeButton("Fechar") { dialog, _ ->
                dialog.dismiss()
            }
        }

        dialog?.show()

    }

    private fun swipe() {

        val swipeHandler = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.START or ItemTouchHelper.END
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {

                val startPosition = viewHolder.adapterPosition
                val endPosition = target.adapterPosition

                Collections.swap(adapter.habits, startPosition, endPosition)
                adapter.notifyItemMoved(startPosition, endPosition)

                return true

            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                when (direction) {

                    ItemTouchHelper.END ->

                        this@HomeFragment.let {
                            Toast.makeText(
                                context,
                                "Done!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    ItemTouchHelper.START ->

                        this@HomeFragment.let {
                            Toast.makeText(
                                context,
                                "Not Done!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                }
            }
        }

        ItemTouchHelper(swipeHandler).attachToRecyclerView(binding.rvHabits)

    }

}