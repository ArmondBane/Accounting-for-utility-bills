package com.goodcomp.accutilitybills.di

import com.goodcomp.accutilitybills.data.repository.NotesRepositoryImpl
import com.goodcomp.accutilitybills.data.repository.StatisticRepositoryImpl
import com.goodcomp.accutilitybills.domain.repository.NotesRepository
import com.goodcomp.accutilitybills.domain.repository.StatisticRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier

@InstallIn(SingletonComponent::class)
@Module
abstract class MainModule {

    @Binds
    abstract fun bindNotesRepository(repository: NotesRepositoryImpl): NotesRepository

    @Binds
    abstract fun bindStatisticRepository(repository: StatisticRepositoryImpl): StatisticRepository
}

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope