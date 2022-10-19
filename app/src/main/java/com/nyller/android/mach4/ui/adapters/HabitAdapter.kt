package com.nyller.android.mach4.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nyller.android.mach4.database.models.Habit
import com.nyller.android.mach4.databinding.ResItemHabitBinding

class HabitAdapter(
    private val onClick: (Habit) -> Unit
) : ListAdapter<Habit, HabitAdapter.HabitViewHolder>(HabitsComparator()) {

    class HabitViewHolder private constructor(val binding: ResItemHabitBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            habit: Habit,
            onClick: (Habit) -> Unit
        ) {
            binding.tvTurn.text = habit.turn
            binding.tvCategory.text = habit.category
            binding.tvHabitName.text = habit.name
            binding.clHabito.setOnClickListener {
                onClick(habit)
            }
        }

        companion object {
            fun from(parent: ViewGroup): HabitViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ResItemHabitBinding.inflate(layoutInflater, parent, false)

                return HabitViewHolder(binding)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitViewHolder {
        return HabitViewHolder.from(parent)
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