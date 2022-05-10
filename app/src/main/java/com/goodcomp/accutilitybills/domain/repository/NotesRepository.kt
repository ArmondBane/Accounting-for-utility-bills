package com.goodcomp.accutilitybills.domain.repository

import com.goodcomp.accutilitybills.domain.entity.CommunalPaymentNote
import com.goodcomp.accutilitybills.domain.entity.PaymentType
import kotlinx.coroutines.flow.Flow

interface NotesRepository {

    fun getCommunalPaymentNoteList(
        searchText: String,
        startDateTime: Long,
        endDateTime: Long
    ): Flow<List<CommunalPaymentNote>>

    suspend fun deleteNote(note: CommunalPaymentNote)

    suspend fun updateNote(note: CommunalPaymentNote)

    suspend fun insertNote(note: CommunalPaymentNote)

    fun getCommunalPaymentNoteListByPayment(type: PaymentType): Flow<List<CommunalPaymentNote>>

    fun getPaymentsTypes(): Flow<List<PaymentType>>
}