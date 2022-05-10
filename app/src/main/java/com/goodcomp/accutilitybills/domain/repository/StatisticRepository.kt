package com.goodcomp.accutilitybills.domain.repository

import kotlinx.coroutines.flow.Flow

interface StatisticRepository {

    suspend fun getStatisticByYearQuarter(
        startDate: Long,
        endDate: Long
    ): Flow<Double?>
}