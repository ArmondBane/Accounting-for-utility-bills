package com.goodcomp.accutilitybills.ui.feature.notes.list

import androidx.lifecycle.viewModelScope
import com.goodcomp.accutilitybills.domain.entity.CommunalPaymentNote
import com.goodcomp.accutilitybills.domain.repository.NotesRepository
import com.goodcomp.accutilitybills.ui.feature.base.BaseAppViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class NoteListViewModel @Inject constructor(
    private val notesRepository: NotesRepository
) : BaseAppViewModel() {

    private val _startDateTime = MutableStateFlow<Calendar>(Calendar.getInstance())
    val startDateTime: StateFlow<Calendar>
        get() = _startDateTime

    private val _endDateTime = MutableStateFlow<Calendar>(Calendar.getInstance())
    val endDateTime: MutableStateFlow<Calendar>
        get() = _endDateTime

    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String>
        get() = _searchText

    val notesList = combine(
        startDateTime,
        endDateTime,
        searchText
    ) { startDateTime, endDateTime, searchText ->
        Triple(searchText, startDateTime, endDateTime)
    }.flatMapLatest {
        notesRepository.getCommunalPaymentNoteList(
            it.first,
            it.second.timeInMillis,
            it.third.timeInMillis
        )
    }

    fun onNoteClick(note: CommunalPaymentNote) {
        emitEvent(Event.NavigateToEditNote(note))
    }

    fun onNoteSwiped(note: CommunalPaymentNote) = viewModelScope.launch {
        notesRepository.deleteNote(note)
    }

    fun onStartDatePicked(calendar: Calendar) {
        _startDateTime.value = calendar
    }

    fun onEndDatePicked(calendar: Calendar) {
        _endDateTime.value = calendar
    }

    fun onSearchTextChanged(searchText: String) {
        _searchText.value = searchText
    }

    fun onAddNewNoteButtonClick() {
        emitEvent(Event.NavigateToAddNewNote)
    }

    fun onToStatisticButtonClick() {
        emitEvent(Event.NavigateToStatistic)
    }

    sealed class Event : BaseEvent {
        data class NavigateToEditNote(val note: CommunalPaymentNote) : Event()
        object NavigateToAddNewNote : Event()
        object NavigateToStatistic : Event()
    }
}