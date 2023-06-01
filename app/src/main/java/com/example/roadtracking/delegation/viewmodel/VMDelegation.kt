package com.example.roadtracking.delegation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.roadtracking.base.Effect
import com.example.roadtracking.base.Event
import com.example.roadtracking.base.State
import kotlinx.coroutines.flow.SharedFlow

interface VMDelegation<EFFECT : Effect, EVENT : Event, STATE : State> {
    fun viewModel(viewModel: ViewModel)

    fun setEffect(effect: EFFECT)

    fun setEvent(event: EVENT)

    fun setState(state: STATE)

    fun getCurrentState(): STATE
    val effect: SharedFlow<EFFECT>
    val event: SharedFlow<EVENT>
    val state: SharedFlow<STATE>
}