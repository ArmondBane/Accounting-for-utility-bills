package com.goodcomp.accutilitybills.ui.feature.statistic.year_quarter_payment

import com.goodcomp.accutilitybills.domain.repository.StatisticRepository
import com.goodcomp.accutilitybills.ui.feature.base.BaseAppViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import java.util.*
import javax.inject.Inject

@HiltViewModel
class DatePaymentStatisticViewModel @Inject constructor(
    private val statisticRepository: StatisticRepository
) : BaseAppViewModel() {

    private val startDateTime = MutableStateFlow<Calendar>(Calendar.getInstance())
    private val endDateTime = MutableStateFlow<Calendar>(Calendar.getInstance())

    private val _year = MutableStateFlow<Calendar>(Calendar.getInstance())
    val year: StateFlow<Calendar>
        get() = _year

    private val _quarter = MutableStateFlow(Quarter.ALL)
    val quarter: StateFlow<Quarter>
        get() = _quarter

    val statisticValue = combine(
        startDateTime,
        endDateTime
    ) { startDateTime, endDateTime ->
        Pair(startDateTime, endDateTime)
    }.flatMapLatest {
        statisticRepository.getStatisticByYearQuarter(
           it.first.timeInMillis,
           it.second.timeInMillis
        )
    }

    fun onYearWasPicked(calendar: Calendar) {
        _year.value = calendar
        updateStatisticDates()
    }

    fun onQuarterPicked(quarter: Quarter) {
        _quarter.value = quarter
        updateStatisticDates()
    }

    private fun updateStatisticDates() {
        startDateTime.value = Calendar.getInstance().apply {
            set(Calendar.YEAR, year.value.get(Calendar.YEAR))
            set(Calendar.MONTH, quarter.value.startMonth)
            set(Calendar.DAY_OF_MONTH, 1)
        }
        endDateTime.value = Calendar.getInstance().apply {
            set(Calendar.YEAR, year.value.get(Calendar.YEAR))
            set(Calendar.MONTH, quarter.value.endMonth)
            set(Calendar.DAY_OF_MONTH, getActualMaximum(Calendar.DAY_OF_MONTH))
        }
    }

    init {
        updateStatisticDates()
    }

    enum class Quarter(val startMonth: Int, val endMonth: Int) {
        ALL(0,11),
        Q1(0,2),
        Q2(3,5),
        Q3(6,8),
        Q4(9,11),
    }
}