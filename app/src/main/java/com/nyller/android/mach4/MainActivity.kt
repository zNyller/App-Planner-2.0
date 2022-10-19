package com.nyller.android.mach4

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
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

    private lateinit var adapter: HabitAdapter

    private lateinit var dialog : AlertDialog

    private val getResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->

        Log.i("Edu", "Resultado: ${result.resultCode}")

        if(result.resultCode == RESULT_OK) {
            val newHabit = result.data?.getSerializableExtra(NewHabitActivity.EXTRA_REPLY) as Habit
            mHabitViewModel.insert(newHabit)
            Log.i("Edu", "OK")
        }

    }

    private val mHabitViewModel: HabitViewModel by viewModels {
        HabitViewModel.HabitViewModelFactory((application as MyApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.habitViewModel = mHabitViewModel

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

        mHabitViewModel.allHabits.observe(this) { habits ->
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

    private fun showHabitDetails(habitSelected: Habit, onHabitStatusChanged: (Habit) -> Unit) {

        //dialog personalizada
        val build = AlertDialog.Builder(this)

        val dialogCustomBinding : DialogCustomBinding = DialogCustomBinding.inflate(LayoutInflater.from(this))

        dialogCustomBinding.tvHabitNameDialog.text = "Nome: ${habitSelected.name}"
        dialogCustomBinding.tvHabitTurnDialog.text = "Turno: ${habitSelected.turn}"
        dialogCustomBinding.tvHabitCategoryDialog.text = "Categoria: ${habitSelected.category}"

        dialogCustomBinding.btnClose.setOnClickListener { dialog.dismiss() }
        dialogCustomBinding.btnDone.setOnClickListener {
            if (!habitSelected.done) {
                dialogCustomBinding.btnDone.text = getString(R.string.concluido)
                habitSelected.done = !habitSelected.done
                onHabitStatusChanged(habitSelected)
                return@setOnClickListener
            }

            else {
                dialogCustomBinding.btnDone.text = getString(R.string.concluir)
                habitSelected.done = !habitSelected.done
                onHabitStatusChanged(habitSelected)
                return@setOnClickListener
            }

        }

        dialogCustomBinding.btnDelete.setOnClickListener {
            mHabitViewModel.delete(habitSelected)
            dialog.dismiss()
            Toast.makeText(this, "Hábito excluído!", Toast.LENGTH_SHORT).show()
        }

        build.setView(dialogCustomBinding.root)

        dialog = build.create()
        dialog.show()
    }

}