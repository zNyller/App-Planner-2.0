package com.nyller.android.mach4

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.nyller.android.mach4.database.models.Habit

@BindingAdapter("itemNameHabitTv")
fun TextView.setHabitString(item: Habit?) {
    item?.let {
        text = item.name
    }
}