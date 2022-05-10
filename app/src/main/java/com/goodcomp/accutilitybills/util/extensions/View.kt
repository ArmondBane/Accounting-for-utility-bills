package com.goodcomp.accutilitybills.util.extensions

import android.app.DatePickerDialog
import android.content.Context
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import kotlinx.coroutines.flow.StateFlow
import java.util.*

@RequiresApi(Build.VERSION_CODES.N)
fun View.setOnDatePickerListener(
    context: Context,
    date: StateFlow<Calendar>? = null,
    onDatePicked: (Calendar) -> Unit
) {
    setOnClickListener {
        DatePickerDialog(context).apply {
            setOnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                onDatePicked(
                    Calendar.getInstance().apply {
                        set(year, monthOfYear, dayOfMonth)
                    }
                )
            }
            val calendarNow = Calendar.getInstance()
            updateDate(
                date?.value?.get(Calendar.YEAR)
                    ?: calendarNow.get(Calendar.YEAR),
                date?.value?.get(Calendar.MONTH)
                    ?: calendarNow.get(Calendar.MONTH),
                date?.value?.get(Calendar.DAY_OF_MONTH)
                    ?: calendarNow.get(Calendar.DAY_OF_MONTH)
            )
        }.show()
    }
}