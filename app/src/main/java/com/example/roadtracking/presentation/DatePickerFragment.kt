package com.example.roadtracking.presentation

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.roadtracking.common.extensions.dateToStringConvert
import com.example.roadtracking.presentation.home.HomeUIEvent
import com.example.roadtracking.presentation.home.HomeVM
import java.util.Calendar


class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {
    private lateinit var viewmodel: HomeVM
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        val senDate = calendar.time.dateToStringConvert()
        viewmodel.setEvent(HomeUIEvent.ResultData(senDate))
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        viewmodel = ViewModelProvider(requireActivity())[HomeVM::class.java]
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        return DatePickerDialog(activity as Context, this, year, month, day)
    }
}

class MonthPickerDialogFragment : DialogFragment() {
    private lateinit var viewmodel: HomeVM
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        viewmodel = ViewModelProvider(requireActivity())[HomeVM::class.java]
        val months = arrayOf(
            "Ocak",
            "Şubat",
            "Mart",
            "Nisan",
            "Mayıs",
            "Haziran",
            "Temmuz",
            "Ağustos",
            "Eylül",
            "Ekim",
            "Kasım",
            "Aralık"
        )

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Ay Seçimi")
            .setItems(months) { dialog, which ->
                viewmodel.setEvent(HomeUIEvent.SelectedMonth(which + 1))
                dialog.dismiss()
            }
        return builder.create()
    }
}