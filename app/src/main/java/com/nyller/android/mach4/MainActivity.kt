package com.nyller.android.mach4

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.nyller.android.mach4.adapter.HabitAdapter
import com.nyller.android.mach4.constants.Constants.EXTRA_NEW_HABIT
import com.nyller.android.mach4.databinding.ActivityMainBinding
import com.nyller.android.mach4.model.Habit
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: HabitAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAdapter()

    }

    override fun onStart() {
        super.onStart()

        swipe()
        binding.fabNewHabit.setOnClickListener { openNewHabit() }

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

                        this@MainActivity.let {
                            Toast.makeText(
                                this@MainActivity,
                                "Done!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    ItemTouchHelper.START ->

                        this@MainActivity.let {
                            Toast.makeText(
                                this@MainActivity,
                                "Not Done!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                }
            }
        }

        ItemTouchHelper(swipeHandler).attachToRecyclerView(binding.rvHabits)

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

        val dialog = AlertDialog.Builder(this)
        dialog.apply {
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

        dialog.show()

    }

    private val getResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->

        if (result.resultCode != RESULT_OK) {
            return@registerForActivityResult
        }

        val habit = result.data?.extras?.getSerializable(EXTRA_NEW_HABIT) as Habit
        adapter.addHabit(habit)

    }

    private fun openNewHabit() {

        getResult.launch(Intent(this, NewHabitActivity::class.java))

    }

}