package com.goodcomp.accutilitybills.data.repository

import com.goodcomp.accutilitybills.data.local.source.LocalDataSource
import com.goodcomp.accutilitybills.domain.repository.StatisticRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class StatisticRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource
) : StatisticRepository {

    override suspend fun getStatisticByYearQuarter(
        startDate: Long,
        endDate: Long
    ): Flow<Double?> =
        localDataSource.getAveragePaymentByDate(
            startDate,
            endDate
        )
}