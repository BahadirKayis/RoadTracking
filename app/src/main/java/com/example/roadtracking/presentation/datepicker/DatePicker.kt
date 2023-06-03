package com.example.roadtracking.presentation.datepicker

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

class DatePicker {

    class DateSelectionDialogFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {
        private lateinit var viewModel: HomeVM
        override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)
            val senDate = calendar.time.dateToStringConvert()
            viewModel.setEvent(HomeUIEvent.ResultData(senDate))
        }

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            viewModel = ViewModelProvider(requireActivity())[HomeVM::class.java]
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            return DatePickerDialog(activity as Context, this, year, month, day)
        }
    }

    class MonthSelectionDialogFragment : DialogFragment() {
        private lateinit var viewModel: HomeVM
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            viewModel = ViewModelProvider(requireActivity())[HomeVM::class.java]
            val months = arrayOf(
                "Ocak/01",
                "Şubat/02",
                "Mart/03",
                "Nisan/04",
                "Mayıs/05",
                "Haziran/06",
                "Temmuz/07",
                "Ağustos/08",
                "Eylül/09",
                "Ekim/10",
                "Kasım/11",
                "Aralık/12"
            )

            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Ay Seçimi")
                .setItems(months) { dialog, which ->
                    viewModel.setEvent(HomeUIEvent.SelectedMonth(which + 1))
                    dialog.dismiss()
                }
            return builder.create()
        }
    }
}