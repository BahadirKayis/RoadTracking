package com.example.roadtracking.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roadtracking.common.extensions.collectIn
import com.example.roadtracking.common.extensions.dateToLongConvert
import com.example.roadtracking.common.extensions.titleCaseFirstChar
import com.example.roadtracking.data.model.RoadUI
import com.example.roadtracking.delegation.viewmodel.VMDelegation
import com.example.roadtracking.delegation.viewmodel.VMDelegationImpl
import com.example.roadtracking.domain.converter.RoadExpensePdfConverter
import com.example.roadtracking.domain.usecase.DeleteRoadItemUseCae
import com.example.roadtracking.domain.usecase.GetMonthUseCase
import com.example.roadtracking.domain.usecase.GetRoadUseCase
import com.example.roadtracking.domain.usecase.SearchUseCase
import com.example.roadtracking.domain.usecase.SetRoadUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeVM @Inject constructor(
    private val getRoadUseCase: GetRoadUseCase,
    private val setRoadUseCase: SetRoadUseCase,
    private val searchUseCase: SearchUseCase,
    private val getMonthUseCase: GetMonthUseCase,
    private val deleteRoadItemUseCase: DeleteRoadItemUseCae,
    private val pdfConverter: RoadExpensePdfConverter,
) : ViewModel(),
    VMDelegation<HomeUIEffect, HomeUIEvent, HomeUIState> by VMDelegationImpl(HomeUIState()) {
    init {
        viewModel(this)
        event.collectIn(viewModelScope) { event ->
            when (event) {
                is HomeUIEvent.ShowDatePicker -> setEffect(HomeUIEffect.ShowDatePicker)
                is HomeUIEvent.SaveData -> setRoadData(event.date, event.company)
                is HomeUIEvent.ResultData -> setState(getCurrentState().copy(date = event.date))
                is HomeUIEvent.SearchCompany -> searchCompany(event.company)
                is HomeUIEvent.SelectMonth -> setEffect(HomeUIEffect.ShowMonthPicker)
                is HomeUIEvent.DeleteRoadItem -> deleteRoadItem(event.roadUI)
                is HomeUIEvent.SelectedMonth -> getMonthAndConvertToPdf(event.mont)
            }
        }
        getRoadData()
    }

    private fun getMonthAndConvertToPdf(month: Int) {
        getMonthUseCase(month).onEach {
            setEffect(HomeUIEffect.SendMonth(pdfConverter.createPdf(it, month)))
        }.launchIn(viewModelScope)
    }

    private fun getRoadData() {
        getRoadUseCase().onEach {
            setState(HomeUIState(roadUI = it))
        }.launchIn(viewModelScope)
    }

    private fun setRoadData(date: String, company: String) = viewModelScope.launch {
        if (date.isNotEmpty() && company.isNotEmpty()) {
            setRoadUseCase(
                RoadUI(
                    dateInMillis = date.dateToLongConvert(),
                    company = company.titleCaseFirstChar()
                )
            )
        } else setEffect(HomeUIEffect.ShowToast("Tarih ve firma boş bırakılamaz"))
    }

    private fun deleteRoadItem(road: RoadUI) = viewModelScope.launch {
        deleteRoadItemUseCase(road)
    }

    private fun searchCompany(company: String) {
        searchUseCase(company.titleCaseFirstChar()).onEach {
            setState(getCurrentState().copy(companyList = it))
        }.launchIn(viewModelScope)
    }

}