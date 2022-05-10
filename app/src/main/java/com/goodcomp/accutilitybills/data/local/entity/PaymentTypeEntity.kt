package com.goodcomp.accutilitybills.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.goodcomp.accutilitybills.domain.entity.PaymentType

@Entity(tableName = PaymentTypeEntity.TABLE_NAME)
data class PaymentTypeEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = COLUMN_ID)
    val id: Int,

    @ColumnInfo(name = COLUMN_NAME)
    val name: String,

    @ColumnInfo(name = COLUMN_COUNTER_AVAILABLE)
    val counterAvailable: Boolean,
) {

    fun toPaymentType() =
        PaymentType(
            id = id,
            name = name,
            counterAvailable = counterAvailable
        )

    companion object {
        const val TABLE_NAME = "payment_type_table"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_COUNTER_AVAILABLE = "counter_available"
    }
}