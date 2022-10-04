package com.nyller.android.mach4.model

import java.io.Serializable

data class Habit(
    var name: String ?= "null",
    var turn: String ?= "null",
    var category: String ?= "null",
    var done: Boolean = false
) : Serializable
