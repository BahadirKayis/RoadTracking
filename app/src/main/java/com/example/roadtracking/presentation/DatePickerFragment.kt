package com.example.roadtracking.presentation

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
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