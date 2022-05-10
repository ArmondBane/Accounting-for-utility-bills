package com.goodcomp.accutilitybills.domain.entity

import android.os.Parcelable
import com.goodcomp.accutilitybills.data.local.entity.CommunalPaymentNoteEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class CommunalPaymentNote(
    private val id: Int,
    val dateTime: Long,
    val paymentType: PaymentType,
    val meterReading: Int,
    val paymentAmount: Double
) : Parcelable {

    fun toDataBaseEntity() =
        CommunalPaymentNoteEntity(
            id = id,
            dateTime = dateTime,
            paymentTypeId = paymentType.toDataBaseEntity().id,
            meterReading = meterReading,
            paymentAmount = paymentAmount
        )
}