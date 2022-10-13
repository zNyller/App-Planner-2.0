package com.nyller.android.mach4.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nyller.android.mach4.databinding.ResItemHabitBinding
import com.nyller.android.mach4.database.models.Habit

class HabitAdapter(
    private val onClick: (Habit) -> Unit
) : ListAdapter<Habit, HabitAdapter.HabitViewHolder>(HabitsComparator()) {

    class HabitViewHolder(itemView: ResItemHabitBinding) :
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
        val current = getItem(position)
        holder.bind(
            current,
            onClick
        )
    }

    class HabitsComparator : DiffUtil.ItemCallback<Habit>() {

        override fun areItemsTheSame(oldItem: Habit, newItem: Habit): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Habit, newItem: Habit): Boolean {
            return oldItem.name == newItem.name
        }

    }

}