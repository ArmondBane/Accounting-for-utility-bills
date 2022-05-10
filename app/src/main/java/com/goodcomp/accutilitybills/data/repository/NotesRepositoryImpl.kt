package com.goodcomp.accutilitybills.data.repository

import com.goodcomp.accutilitybills.data.local.entity.CommunalPaymentNoteEntity
import com.goodcomp.accutilitybills.data.local.source.LocalDataSource
import com.goodcomp.accutilitybills.domain.entity.CommunalPaymentNote
import com.goodcomp.accutilitybills.domain.entity.PaymentType
import com.goodcomp.accutilitybills.domain.repository.NotesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NotesRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource
) : NotesRepository {

    override fun getCommunalPaymentNoteList(
        searchText: String,
        startDateTime: Long,
        endDateTime: Long
    ): Flow<List<CommunalPaymentNote>> =
        localDataSource.getCommunalPaymentNoteList(
            searchText,
            startDateTime,
            endDateTime
        ).mapToCommunalPaymentNoteList(localDataSource)

    override suspend fun deleteNote(note: CommunalPaymentNote) =
        localDataSource.deleteNote(note.toDataBaseEntity())

    override suspend fun updateNote(note: CommunalPaymentNote) =
        localDataSource.updateNote(note.toDataBaseEntity())

    override suspend fun insertNote(note: CommunalPaymentNote) =
        localDataSource.insertNote(note.toDataBaseEntity())

    override fun getCommunalPaymentNoteListByPayment(type: PaymentType) =
        localDataSource.getCommunalPaymentNoteListByPaymentType(type.toDataBaseEntity())
            .mapToCommunalPaymentNoteList(localDataSource)

    override fun getPaymentsTypes() =
        localDataSource.getPaymentTypes().map { it.map { type -> type.toPaymentType() } }
}

fun Flow<List<CommunalPaymentNoteEntity>>.mapToCommunalPaymentNoteList(
    localDataSource: LocalDataSource
) = map {
        it.map { note ->
            localDataSource.getPaymentTypeById(note.paymentTypeId).first().let { type ->
                note.toCommunalPaymentNote(type.toPaymentType())
            }
        }
    }
