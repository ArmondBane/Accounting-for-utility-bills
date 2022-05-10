package com.goodcomp.accutilitybills.data.local.dao

import androidx.room.*
import com.goodcomp.accutilitybills.data.local.entity.CommunalPaymentNoteEntity
import com.goodcomp.accutilitybills.data.local.entity.CommunalPaymentNoteEntity.Companion as CPNote
import com.goodcomp.accutilitybills.data.local.entity.PaymentTypeEntity.Companion as PT
import kotlinx.coroutines.flow.Flow


@Dao
interface CommunalPaymentNoteDao {

    @Query(
        "SELECT * FROM ${CPNote.TABLE_NAME} " +
        "WHERE ${CPNote.TABLE_NAME}.payment_type_id = :id"
    )
    fun getCommunalPaymentNoteListByPaymentTypeId(
        id: Int
    ): Flow<List<CommunalPaymentNoteEntity>>

    @Query(
        "SELECT ${CPNote.TABLE_NAME}.* FROM ${CPNote.TABLE_NAME}, ${PT.TABLE_NAME} " +
        "WHERE ${CPNote.TABLE_NAME}.date_time >= :startDateTime AND " +
        "${CPNote.TABLE_NAME}.date_time <= :endDateTime AND " +
        "${PT.TABLE_NAME}.name LIKE '%' || :searchQuery || '%' AND " +
        "${PT.TABLE_NAME}.id = ${CPNote.TABLE_NAME}.payment_type_id"
    )
    fun getCommunalPaymentNoteList(
        searchQuery: String,
        startDateTime: Long,
        endDateTime: Long
    ): Flow<List<CommunalPaymentNoteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCommunalPaymentNote(note: CommunalPaymentNoteEntity)

    @Update
    suspend fun updateCommunalPaymentNote(note: CommunalPaymentNoteEntity)

    @Delete
    suspend fun deleteCommunalPaymentNote(note: CommunalPaymentNoteEntity)
}