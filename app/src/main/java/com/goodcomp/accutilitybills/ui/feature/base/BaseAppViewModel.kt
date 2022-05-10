package com.goodcomp.accutilitybills.ui.feature.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

abstract class BaseAppViewModel : ViewModel() {

    private val _event = MutableSharedFlow<BaseEvent>()
    val event: SharedFlow<BaseEvent>
        get() = _event

    protected fun emitEvent(event: BaseEvent) = viewModelScope.launch {
        _event.emit(event)
    }

    interface BaseEvent

    data class ShowToastMassage(val message: String) : BaseEvent
}