package com.example.roadtracking.presentation.home


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.content.FileProvider
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.roadtracking.R
import com.example.roadtracking.common.SwipeToDeleteCallback
import com.example.roadtracking.common.extensions.collectIn
import com.example.roadtracking.common.extensions.snackBar
import com.example.roadtracking.databinding.FragmentHomeBinding
import com.example.roadtracking.delegation.viewBinding
import com.example.roadtracking.presentation.datepicker.DatePicker
import dagger.hilt.android.AndroidEntryPoint
import java.io.File


@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {
    private val binding by viewBinding(FragmentHomeBinding::bind)
    private lateinit var viewModel: HomeVM
    private val adapter by lazy { RoadAdapter() }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[HomeVM::class.java]
        initUIEvent()
        initUIEffect()
        initUIState()
        binding.acTextCompany.clearFocus()
        binding.acTextCompany.doOnTextChanged { text, _, _, _ ->
            viewModel.setEvent(HomeUIEvent.SearchCompany(text.toString()))
        }
    }

    private fun initUIEvent() {
        with(viewModel) {
            binding.etDate.setOnClickListener {
                setEvent(HomeUIEvent.ShowDatePicker)
            }
            binding.buttonSave.setOnClickListener {
                setEvent(
                    HomeUIEvent.SaveData(
                        binding.etDate.text.toString(),
                        binding.acTextCompany.text.toString()
                    )
                )
                binding.etDate.text.clear()
                binding.acTextCompany.text.clear()
            }

            binding.buttonSend.setOnClickListener {

                setEvent(HomeUIEvent.SelectMonth)
            }

            val swipeHandler = object : SwipeToDeleteCallback(requireContext()) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val task = adapter.currentList[viewHolder.layoutPosition]
                    viewModel.setEvent(HomeUIEvent.DeleteRoadItem(task))
                }
            }
            val itemTouchHelper = ItemTouchHelper(swipeHandler)
            itemTouchHelper.attachToRecyclerView(binding.rcRoad)
        }
    }

    private fun initUIEffect() = viewModel.effect.collectIn(viewLifecycleOwner) { effect ->
        when (effect) {
            is HomeUIEffect.ShowDatePicker -> {
                val dialogFragment = DatePicker.DateSelectionDialogFragment()
                dialogFragment.show(requireActivity().supportFragmentManager, DATE_SELECTION)
            }

            is HomeUIEffect.ShowToast -> requireView().snackBar(effect.message)

            is HomeUIEffect.SendMonth -> {
                sendPdf(effect.pdfFile)

            }

            HomeUIEffect.ShowMonthPicker -> {
                val dialogFragment = DatePicker.MonthSelectionDialogFragment()
                dialogFragment.show(requireActivity().supportFragmentManager, MONTH_SELECTION)
            }
        }
    }

    private fun initUIState() = viewModel.state.collectIn(viewLifecycleOwner) { state ->
        with(state) {
            date?.let {
                binding.etDate.setText(it)
            }
            roadUI?.let {
                adapter.submitList(it)
                binding.rcRoad.adapter = adapter
            }
            companyList?.let {
                autoCompleteAdapter(it)
            }
        }
    }

    private fun autoCompleteAdapter(company: List<String>) {
        val adapter =
            ArrayAdapter(requireContext(), R.layout.dropdown_ch, company)
        binding.acTextCompany.setAdapter(adapter)
        binding.acTextCompany.threshold = 1
    }

    private fun sendPdf(file: File) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = SHARE_TYPE
        shareIntent.putExtra(
            Intent.EXTRA_STREAM,
            FileProvider.getUriForFile(requireContext(), AUTH, file)
        )
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)


        context?.startActivity(Intent.createChooser(shareIntent, TITLE))
    }

    companion object {
        const val DATE_SELECTION = "dateSelection"
        const val MONTH_SELECTION = "monthSelection"
        const val SHARE_TYPE = "application/pdf"
        const val TITLE = "Share PDF"
        const val AUTH = "com.bahadir.RoadTracking"
    }
}
