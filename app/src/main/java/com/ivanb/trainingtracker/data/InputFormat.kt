package com.ivanb.trainingtracker.data

fun Double.toInputString(): String =
    if (this % 1.0 == 0.0) toInt().toString() else toString()