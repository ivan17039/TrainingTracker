package com.ivanb.trainingtracker

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Long.toFormattedDate(): String {
    val sdf = SimpleDateFormat("d. M. yyyy.", Locale.getDefault())
    return sdf.format(Date(this))
}