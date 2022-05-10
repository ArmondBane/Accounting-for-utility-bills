package com.goodcomp.accutilitybills.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import com.goodcomp.accutilitybills.data.local.entity.CommunalPaymentNoteEntity.Companion as CPNote


@Dao
interface StatisticDao {

    @Query(
        "SELECT AVG(${CPNote.TABLE_NAME}.payment_amount) FROM ${CPNote.TABLE_NAME} " +
        "WHERE ${CPNote.COLUMN_DATE_TIME} >= :startDateTime AND ${CPNote.COLUMN_DATE_TIME} <= :endDateTime"
    )
    fun getAveragePaymentByDate(
        startDateTime: Long,
        endDateTime: Long
    ): Flow<Double?>
}