package com.example.roadtracking.presentation.home

import com.example.roadtracking.base.State
import com.example.roadtracking.data.model.RoadUI


data class HomeUIState(
    var isLoading: Boolean = false,
    val date: String? = null,
    val roadUI: List<RoadUI>? = null,
    val companyList: List<String>? = null
) : State