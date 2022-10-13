package com.nyller.android.mach4

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.nyller.android.mach4.activities.NewHabitActivity
import com.nyller.android.mach4.application.MyApplication
import com.nyller.android.mach4.database.models.Habit
import com.nyller.android.mach4.databinding.ActivityMainBinding
import com.nyller.android.mach4.databinding.DialogCustomBinding
import com.nyller.android.mach4.ui.adapters.HabitAdapter
import com.nyller.android.mach4.ui.viewmodel.HabitViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var adapter: HabitAdapter

    private lateinit var dialog : AlertDialog

    private val getResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->

        Log.i("Edu", "Resultado: ${result.resultCode}")

        if(result.resultCode == RESULT_OK) {
            val newHabit = result.data?.getSerializableExtra(NewHabitActivity.EXTRA_REPLY) as Habit
            habitViewModel.insert(newHabit)
            Log.i("Edu", "OK")
        }

    }

    private val habitViewModel: HabitViewModel by viewModels {
        HabitViewModel.HabitViewModelFactory((application as MyApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = HabitAdapter(
            onClick = { habit ->
                showHabitDetails(habit) { habitSelected ->
                    // Atualizar adapter
                }
            }
        )
        binding.rvHabits.adapter = adapter

        binding.fabNewHabit.setOnClickListener {
            val intent = Intent(this, NewHabitActivity::class.java)
            getResult.launch(intent)
        }

        habitViewModel.allHabits.observe(this) { habits ->
            habits?.let { adapter.submitList(it) }
        }
    }

    override fun onStart() {
        super.onStart()

//        val navHostFragment = supportFragmentManager.findFragmentById(binding.navHostFragment.id) as NavHostFragment
//        val navController = navHostFragment.navController
//
//        binding.bottomNavigationView.setupWithNavController(navController)

    }

    private fun showHabitDetails(habit: Habit, onHabitStatusChanged: (Habit) -> Unit) {

        //dialog personalizada
        val build = AlertDialog.Builder(this)

        val dialogCustomBinding : DialogCustomBinding = DialogCustomBinding.inflate(LayoutInflater.from(this))

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
        }

        build.setView(dialogCustomBinding.root)

        dialog = build.create()
        dialog.show()
    }

}