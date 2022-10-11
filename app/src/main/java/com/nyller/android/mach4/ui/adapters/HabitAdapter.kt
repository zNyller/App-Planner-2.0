package com.nyller.android.mach4.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nyller.android.mach4.databinding.ResItemHabitBinding
import com.nyller.android.mach4.database.models.Habit

class HabitAdapter(
    private val onClick: (Habit) -> Unit
) : RecyclerView.Adapter<HabitAdapter.HabitViewHolder>() {

    val habits = mutableListOf<Habit>()

    inner class HabitViewHolder(itemView: ResItemHabitBinding) :
        RecyclerView.ViewHolder(itemView.root) {

        private val tvTurn = itemView.tvTurn
        private val tvCategory = itemView.tvCategory
        private val tvHabitName = itemView.tvHabitName
        private val clHabit = itemView.clHabito

        fun bind(
            habit: Habit,
            onClick: (Habit) -> Unit
        ) {

            tvTurn.text = habit.turn
            tvCategory.text = habit.category
            tvHabitName.text = habit.name
            clHabit.setOnClickListener {
                onClick(habit)
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitViewHolder {
        return HabitViewHolder(
            ResItemHabitBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: HabitViewHolder, position: Int) {
        holder.bind(
            habits[position],
            onClick
        )
    }

    override fun getItemCount(): Int = habits.size

    fun addHabit(habit: List<Habit>){

        habits.addAll(habit)
        notifyItemInserted(habits.size - 1)

    }

    fun updateHabit(habitSelected: Habit) {

        val updatedPosition = habits.indexOf(habitSelected)
        habits[updatedPosition] = habitSelected
        notifyItemChanged(updatedPosition)
        Log.i("Edu", "Mudei o ${habitSelected.name} para ${habitSelected.done}")

    }

    fun deleteHabit(habit: Habit) {
        val deletedPosition = habits.indexOf(habit)
        habits.remove(habit)
        notifyItemRemoved(deletedPosition)
    }

}