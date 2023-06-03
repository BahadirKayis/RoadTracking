package com.example.roadtracking.common.extensions

import android.view.View
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun View.snackBar(string: String) {
    Snackbar.make(this, string, Snackbar.LENGTH_SHORT).show()
}

fun Date.dateToStringConvert(): String {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return dateFormat.format(time)
}

fun String.dateToLongConvert(): Long {
    val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val date = format.parse(this)
    return date!!.time
}

fun Long.longToDateConvert(): String {
    val calendarT = Calendar.getInstance()
    calendarT.timeInMillis = this
    return calendarT.time.dateToStringConvert()
}

fun String.titleCaseFirstChar() = replaceFirstChar(Char::titlecase)
