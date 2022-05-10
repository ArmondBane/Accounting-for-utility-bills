package com.goodcomp.accutilitybills.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.goodcomp.accutilitybills.domain.entity.CommunalPaymentNote
import com.goodcomp.accutilitybills.domain.entity.PaymentType

@Entity(tableName = CommunalPaymentNoteEntity.TABLE_NAME)
data class CommunalPaymentNoteEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = COLUMN_ID)
    val id: Int,

    @ColumnInfo(name = COLUMN_DATE_TIME)
    val dateTime: Long,

    @ColumnInfo(name = COLUMN_PAYMENT_TYPE_ID)
    val paymentTypeId: Int,

    @ColumnInfo(name = COLUMN_PAYMENT_METER_READING)
    val meterReading: Int,

    @ColumnInfo(name = COLUMN_PAYMENT_PAYMENT_AMOUNT)
    val paymentAmount: Double
) {

    fun toCommunalPaymentNote(paymentType: PaymentType) =
        CommunalPaymentNote(
            id = id,
            dateTime = dateTime,
            paymentType = paymentType,
            meterReading = meterReading,
            paymentAmount = paymentAmount
        )

    companion object {
        const val TABLE_NAME = "communal_payment_note_table"
        const val COLUMN_ID = "id"
        const val COLUMN_DATE_TIME = "date_time"
        const val COLUMN_PAYMENT_TYPE_ID = "payment_type_id"
        const val COLUMN_PAYMENT_METER_READING = "meter_reading"
        const val COLUMN_PAYMENT_PAYMENT_AMOUNT = "payment_amount"
    }
}