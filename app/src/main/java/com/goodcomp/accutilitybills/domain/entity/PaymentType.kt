package com.goodcomp.accutilitybills.domain.entity

import android.os.Parcelable
import com.goodcomp.accutilitybills.data.local.entity.PaymentTypeEntity
import kotlinx.parcelize.Parcelize

@Parcelize
class PaymentType(
    private val id: Int,
    val name: String,
    val counterAvailable: Boolean
) : Parcelable {

    fun toDataBaseEntity() =
        PaymentTypeEntity(
            id = id,
            name = name,
            counterAvailable = counterAvailable
        )
}