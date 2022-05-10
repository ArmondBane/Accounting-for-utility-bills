package com.goodcomp.accutilitybills.ui.feature.notes.add_edit

import android.content.Context
import androidx.annotation.StringRes
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.goodcomp.accutilitybills.R
import com.goodcomp.accutilitybills.domain.entity.CommunalPaymentNote
import com.goodcomp.accutilitybills.domain.entity.PaymentType
import com.goodcomp.accutilitybills.domain.repository.NotesRepository
import com.goodcomp.accutilitybills.ui.feature.base.BaseAppViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AddEditNoteViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val notesRepository: NotesRepository,
    state: SavedStateHandle,
) : BaseAppViewModel() {

    private val resources = context.resources

    private val noteToEdit =
        state.get<CommunalPaymentNote>(COMMUNAL_PAYMENT_NOTE_KEY)

    private val isAddNewNote = noteToEdit == null

    val headerTextRes: Int =
        if (isAddNewNote) R.string.header_add_edit_add
        else R.string.header_add_edit_edit

    val buttonTextRes: Int =
        if (isAddNewNote) R.string.button_add_edit_add
        else R.string.button_add_edit_edit

    private val _dateTime = MutableStateFlow<Calendar>(
        Calendar.getInstance().apply {
            if (noteToEdit != null) timeInMillis = noteToEdit.dateTime
        }
    )
    val dateTime: StateFlow<Calendar>
        get() = _dateTime

    private val _isCounterAvailable = MutableStateFlow(
        noteToEdit?.paymentType?.counterAvailable ?: false
    )
    val isCounterAvailable: StateFlow<Boolean>
        get() = _isCounterAvailable

    private val _meterReading = MutableStateFlow(noteToEdit?.meterReading ?: 0)
    val meterReading: StateFlow<Int>
        get() = _meterReading

    private val _paymentAmount = MutableStateFlow(noteToEdit?.paymentAmount ?: 0.0)
    val paymentAmount: StateFlow<Double>
        get() = _paymentAmount

    private val _paymentType = MutableStateFlow(noteToEdit?.paymentType)
    private val paymentType: StateFlow<PaymentType?>
        get() = _paymentType
    val paymentTypeIndex = paymentType.map { type ->
            allPaymentsType.first().run { indexOfFirst { it.name == type?.name } }
        }

    val previousMeterReadingsList = paymentType.flatMapLatest { paymentType ->
        if (paymentType != null) notesRepository.getCommunalPaymentNoteListByPayment(paymentType)
        else flowOf(emptyList())
    }

    val allPaymentsType = notesRepository.getPaymentsTypes()

    fun onDateTimeChanged(calendar: Calendar) {
        _dateTime.value = calendar
    }

    fun onCounterAvailableCheckChanged(checked: Boolean) {
        _isCounterAvailable.value = checked
    }

    fun onMeterReadingTextChanged(text: String) {
        runCatching { _meterReading.value = text.toInt() }
    }

    fun onPaymentAmountTextChanged(text: String) {
        runCatching { _paymentAmount.value = text.toDouble() }
    }

    fun onPaymentTypeSelected(type: PaymentType) {
        _paymentType.value = type
    }

    fun onSaveButtonClick() = viewModelScope.launch {
        val dateTime = dateTime.value.timeInMillis
        val meterReading = meterReading.value
        val paymentAmount = paymentAmount.value
        val paymentType = paymentType.value
        runCatching {
            require(dateTime <= System.currentTimeMillis()) {
               getString(R.string.date_time_error)
            }
            require(meterReading > 0) {
                getString(R.string.meter_reading_error)
            }
            require(paymentAmount > 0) {
                getString(R.string.payment_amount_error)
            }
            require(paymentType != null) {
                getString(R.string.payment_type_error)
            }
            if (isAddNewNote) {
                notesRepository.insertNote(
                    CommunalPaymentNote(
                        id = 0,
                        dateTime = dateTime,
                        paymentType = paymentType,
                        meterReading = meterReading,
                        paymentAmount = paymentAmount
                    )
                )
            } else {
                notesRepository.updateNote(
                    noteToEdit?.copy(
                        dateTime = dateTime,
                        paymentType = paymentType,
                        meterReading = meterReading,
                        paymentAmount = paymentAmount
                    ) ?: return@launch
                )
            }
            emitEvent(Event.NavigateBack)
        }.onFailure {
            emitEvent(ShowToastMassage(it.message ?: it.toString()))
        }
    }

    private fun getString(@StringRes res: Int) =
        resources.getString(res)

    sealed class Event : BaseEvent {
        object NavigateBack : Event()
    }

    companion object {
        const val COMMUNAL_PAYMENT_NOTE_KEY = "Note"
    }
}