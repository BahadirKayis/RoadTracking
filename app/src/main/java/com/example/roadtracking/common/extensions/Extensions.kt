package com.example.roadtracking.common.extensions

import android.content.res.Resources
import android.view.View

import androidx.annotation.ColorRes
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.snackbar.Snackbar
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun View.gone() {
    visibility = View.GONE
}

fun View.visible() {
    visibility = View.VISIBLE
}

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
fun String.isValidDateFormat(): Boolean {
    val dateFormat = "dd/MM/yyyy"
    val inputDateFormat = SimpleDateFormat(dateFormat, Locale.getDefault())
    inputDateFormat.isLenient = false

    return try {
        inputDateFormat.parse(this)
        true
    } catch (e: ParseException) {
        false
    }
}

fun String.titleCaseFirstChar() = replaceFirstChar(Char::titlecase)


fun Resources.colorStateList(@ColorRes color: Int) =
    ResourcesCompat.getColorStateList(this, color, null)

fun Resources.color(@ColorRes color: Int): Int {
    return ResourcesCompat.getColor(this, color, null)
}
