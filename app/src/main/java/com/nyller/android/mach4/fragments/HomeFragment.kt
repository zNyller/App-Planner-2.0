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
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.nyller.android.mach4.activities.NewHabitActivity
import com.nyller.android.mach4.ui.adapters.HabitAdapter
import com.nyller.android.mach4.database.AppDataBase
import com.nyller.android.mach4.database.daos.habitDAO
import com.nyller.android.mach4.databinding.DialogCustomBinding
import com.nyller.android.mach4.databinding.FragmentHomeBinding
import com.nyller.android.mach4.database.models.Habit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class HomeFragment : Fragment() {

    private lateinit var binding : FragmentHomeBinding
    private lateinit var adapter: HabitAdapter
    private lateinit var habit : Habit

    private lateinit var dataBase: AppDataBase
    private lateinit var habitDAO: habitDAO

    private lateinit var dialog : AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        Log.i("Edu", "onCreate")

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        Log.i("Edu", "onCreateView")

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.i("Edu", "onViewCreated")

        this.dataBase = context?.let { AppDataBase.getInstance(it) }!!
        this.habitDAO = this.dataBase.habitDao()


        binding.fabNewHabit.setOnClickListener { openNewHabit() }

    }

    override fun onResume() {
        super.onResume()

        setupAdapter()
        swipe()

        Log.i("Edu", "onResume")
    }

    private fun openNewHabit() {
        parentFragmentManager.popBackStack()
        startActivity(Intent(activity, NewHabitActivity::class.java))
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

        CoroutineScope(Dispatchers.IO).launch {

            val habits = habitDAO.getHabits()

            withContext(Dispatchers.Main) {
                adapter.addHabit(habits)
            }

        }

    }

    private fun showHabitDetails(habit: Habit, onHabitStatusChanged: (Habit) -> Unit) {

        //dialog personalizada (ainda nao sei como salvar o state nos botões de concluir/concluido)
        val build = context?.let { AlertDialog.Builder(it) }

        val dialogCustomBinding : DialogCustomBinding = DialogCustomBinding.inflate(LayoutInflater.from(context))

        dialogCustomBinding.tvHabitNameDialog.text = "Nome: ${habit.name}"
        dialogCustomBinding.tvHabitTurnDialog.text = "Turno: ${habit.turn}"
        dialogCustomBinding.tvHabitCategoryDialog.text = "Categoria: ${habit.category}"

        dialogCustomBinding.btnClose.setOnClickListener { dialog.dismiss() }
        dialogCustomBinding.btnDone.setOnClickListener {
            if (!habit.done) {
                dialogCustomBinding.btnDone.text = "Concluído!"
                habit.done = !habit.done
                onHabitStatusChanged(habit)
                return@setOnClickListener
            }

            else {
                dialogCustomBinding.btnDone.text = "Concluir"
                habit.done = !habit.done
                onHabitStatusChanged(habit)
                return@setOnClickListener
            }

        }
        dialogCustomBinding.btnDelete.setOnClickListener {

            CoroutineScope(Dispatchers.IO).launch {
                habitDAO.delete(habit)

                withContext(Dispatchers.Main) {
                    adapter.deleteHabit(habit)
                    dialog.dismiss()
                }
            }

        }

        build?.setView(dialogCustomBinding.root)

        dialog = build!!.create()
        dialog.show()


//        val dialog = context?.let { AlertDialog.Builder(it) }
//        dialog?.apply {
//            setTitle("Detalhes do Hábito")
//            setMessage(
//                """"
//                Nome: ${habit.name}
//                Categoria: ${habit.category}
//                Turno: ${habit.turn}
//                """.trimMargin()
//            )
//            setPositiveButton(if (habit.done) "Concluído" else "Concluir") { _, _ ->
//                habit.done = !habit.done
//                onHabitStatusChanged(habit)
//            }
//            setNegativeButton("Fechar") { dialog, _ ->
//                dialog.dismiss()
//            }
//        }
//
//        dialog?.show()
//
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