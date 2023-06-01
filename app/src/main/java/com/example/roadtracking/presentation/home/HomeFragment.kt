package com.example.roadtracking.presentation.home


import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.roadtracking.R
import com.example.roadtracking.common.extensions.collectIn
import com.example.roadtracking.common.extensions.snackBar
import com.example.roadtracking.databinding.FragmentHomeBinding
import com.example.roadtracking.delegation.viewBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {
    private val binding by viewBinding(FragmentHomeBinding::bind)
    private lateinit var viewModel: HomeVM

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[HomeVM::class.java]
        initUIEvent()
        initUIEffect()
        initUIState()
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
                if (binding.etDate.text.toString()
                        .isNotEmpty() && binding.acTextCompany.text.toString()
                        .isNotEmpty()
                )
                    setEvent(
                        HomeUIEvent.SaveData(
                            binding.etDate.text.toString(),
                            binding.acTextCompany.text.toString()
                        )
                    )
                else setEffect(HomeUIEffect.ShowToast("Tarih ve firma boş bırakılamaz"))
            }
        }
    }

    private fun initUIEffect() = viewModel.effect.collectIn(viewLifecycleOwner) { effect ->
        when (effect) {
            is HomeUIEffect.ShowDatePicker -> {
                findNavController().navigate(R.id.action_homeFragment_to_datePickerFragment)
            }

            is HomeUIEffect.ShowToast -> requireView().snackBar(effect.message)
        }
    }


    private fun initUIState() = viewModel.state.collectIn(viewLifecycleOwner) { state ->
        with(state) {
            date?.let {
                binding.etDate.setText(it)
            }
            roadUI?.let {
                binding.rcRoad.adapter = RoadAdapter(it)
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
}
