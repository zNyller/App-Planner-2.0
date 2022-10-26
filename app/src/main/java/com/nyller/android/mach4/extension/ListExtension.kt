package com.nyller.android.mach4.extension

import com.nyller.android.mach4.database.models.Habit
import com.nyller.android.mach4.ui.adapters.HabitAdapter

fun List<Habit>.toListOfDataItem() : List<HabitAdapter.DataItem> {

    val grouping = this.groupBy { habit ->
        habit.turn.toString()

    }

    val listDataItem = mutableListOf<HabitAdapter.DataItem>()
    grouping.forEach { mapEntry ->
        listDataItem.add(HabitAdapter.DataItem.Header(mapEntry.key))
        listDataItem.addAll(
            mapEntry.value.map { habit ->  
                HabitAdapter.DataItem.HabitItem(habit)
            }
        )
    }

    return listDataItem

}