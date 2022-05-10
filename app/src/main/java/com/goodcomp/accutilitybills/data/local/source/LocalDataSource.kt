package com.goodcomp.accutilitybills.data.local.source

import com.goodcomp.accutilitybills.data.local.dao.CommunalPaymentNoteDao
import com.goodcomp.accutilitybills.data.local.dao.PaymentTypeDao
import com.goodcomp.accutilitybills.data.local.dao.StatisticDao
import com.goodcomp.accutilitybills.data.local.entity.CommunalPaymentNoteEntity
import com.goodcomp.accutilitybills.data.local.entity.PaymentTypeEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDataSource @Inject constructor(
    private val communalPaymentNoteDao: CommunalPaymentNoteDao,
    private val paymentTypeDao: PaymentTypeDao,
    private val statisticDao: StatisticDao
){
    fun getCommunalPaymentNoteList(
        searchText: String,
        startDateTime: Long,
        endDateTime: Long
    ): Flow<List<CommunalPaymentNoteEntity>> =
        communalPaymentNoteDao.getCommunalPaymentNoteList(
            searchText,
            startDateTime,
            endDateTime
        )

    suspend fun deleteNote(note: CommunalPaymentNoteEntity) =
        communalPaymentNoteDao.deleteCommunalPaymentNote(note)

    suspend fun updateNote(note: CommunalPaymentNoteEntity) =
        communalPaymentNoteDao.updateCommunalPaymentNote(note)

    suspend fun insertNote(note: CommunalPaymentNoteEntity) =
        communalPaymentNoteDao.insertCommunalPaymentNote(note)

    fun getAveragePaymentByDate(
        startDateTime: Long,
        endDateTime: Long
    ) = statisticDao.getAveragePaymentByDate(
        startDateTime,
        endDateTime
    )

    fun getPaymentTypes() =
        paymentTypeDao.getPaymentTypes()

    fun getCommunalPaymentNoteListByPaymentType(type: PaymentTypeEntity) =
        communalPaymentNoteDao.getCommunalPaymentNoteListByPaymentTypeId(type.id)

    fun getPaymentTypeById(id: Int) =
        paymentTypeDao.getPaymentTypeById(id)
}