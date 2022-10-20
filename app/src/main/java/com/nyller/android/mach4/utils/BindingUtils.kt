package com.nyller.android.mach4.utils

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.nyller.android.mach4.database.models.Habit

@BindingAdapter("habitNameFormatted")
fun TextView.setHabitName(item: Habit) {
    text = item.name
}

@BindingAdapter("habitTurnFormatted")
fun TextView.setHabitTurn(item: Habit) {
    text = item.turn
}

@BindingAdapter("habitCategoryFormatted")
fun TextView.setHabitCategory(item: Habit) {
    text = item.category
}