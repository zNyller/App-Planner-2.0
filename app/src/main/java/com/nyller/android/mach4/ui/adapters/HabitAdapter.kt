package com.nyller.android.mach4.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nyller.android.mach4.database.models.Habit
import com.nyller.android.mach4.databinding.HeaderBinding
import com.nyller.android.mach4.databinding.ResItemHabitBinding
import com.nyller.android.mach4.extension.toListOfDataItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val ITEM_VIEW_TYPE_HEADER = 0
private const val ITEM_VIEW_TYPE_ITEM = 1

class HabitAdapter(private val clickListener: HabitClickListener) :
    ListAdapter<HabitAdapter.DataItem, RecyclerView.ViewHolder>(HabitsComparator()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DataItem.Header -> ITEM_VIEW_TYPE_HEADER
            is DataItem.HabitItem -> ITEM_VIEW_TYPE_ITEM
        }
    }

    class HeaderViewHolder(private val binding: HeaderBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DataItem.Header) {
            with(binding) {
                header = item
            }
        }

        companion object {
            fun from(parent: ViewGroup): HeaderViewHolder {
                val binding: HeaderBinding =
                    HeaderBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                return HeaderViewHolder(binding)
            }
        }
    }

    class HabitViewHolder private constructor(private val binding: ResItemHabitBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            habit: Habit,
            clickListener: HabitClickListener
        ) {
            binding.habit = habit
            binding.clickListener = clickListener
        }

        companion object {
            fun from(parent: ViewGroup): HabitViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ResItemHabitBinding.inflate(layoutInflater, parent, false)

                return HabitViewHolder(binding)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_HEADER -> HeaderViewHolder.from(parent)
            ITEM_VIEW_TYPE_ITEM -> HabitViewHolder.from(parent)
            else -> throw ClassCastException("ViewType desconhecido! $viewType")
        }
    }

    fun addHeaderAndSubmitList(list: List<Habit>?) {
        adapterScope.launch {
            val listDataItem = list?.toListOfDataItem()

            withContext(Dispatchers.Main) {
                submitList(listDataItem)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HabitViewHolder -> {
                val currentItem = getItem(position) as DataItem.HabitItem
                holder.bind(
                    currentItem.habit,
                    clickListener
                )
            }
            is HeaderViewHolder -> {
                val item = getItem(position) as DataItem.Header
                holder.bind(item)
            }
        }

    }

    class HabitsComparator : DiffUtil.ItemCallback<DataItem>() {

        override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
            return oldItem == newItem
        }
    }

    class HabitClickListener(val clickListener: (habitId: Habit) -> Unit) {
        fun onClick(habit: Habit) = clickListener(habit)
    }

    sealed class DataItem {

        abstract val id: Long

        data class HabitItem(val habit: Habit) : DataItem() {
            override val id = habit.habitId
        }

        data class Header(val title: String?) : DataItem() {
            override val id = Long.MIN_VALUE
        }

    }

}