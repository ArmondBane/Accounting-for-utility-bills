package com.goodcomp.accutilitybills.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.goodcomp.accutilitybills.data.local.entity.CommunalPaymentNoteEntity
import com.goodcomp.accutilitybills.data.local.entity.PaymentTypeEntity
import com.goodcomp.accutilitybills.domain.entity.PaymentType
import kotlinx.coroutines.flow.Flow

@Dao
interface PaymentTypeDao {

    @Query("SELECT * FROM ${PaymentTypeEntity.TABLE_NAME}")
    fun getPaymentTypes(): Flow<List<PaymentTypeEntity>>

    @Query(
        "SELECT * FROM ${PaymentTypeEntity.TABLE_NAME} " +
        "WHERE ${PaymentTypeEntity.TABLE_NAME}.id = :id"
    )
    fun getPaymentTypeById(id: Int): Flow<PaymentTypeEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPaymentType(note: PaymentTypeEntity)
}