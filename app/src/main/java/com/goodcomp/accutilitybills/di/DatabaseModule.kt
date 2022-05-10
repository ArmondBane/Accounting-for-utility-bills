package com.goodcomp.accutilitybills.di

import android.content.Context
import androidx.room.Room
import com.goodcomp.accutilitybills.data.local.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(
        @ApplicationContext context: Context,
        callback: AppDatabase.Callback
    ) = Room.databaseBuilder(context, AppDatabase::class.java, AppDatabase.DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .addCallback(callback)
            .build()

    @Provides
    fun provideCommunalPaymentNoteDao(appDatabase: AppDatabase) =
        appDatabase.communalPaymentNoteDao()

    @Provides
    fun providePaymentTypeDao(appDatabase: AppDatabase) =
        appDatabase.paymentTypeDao()

    @Provides
    fun provideStatisticDao(appDatabase: AppDatabase) =
        appDatabase.statisticDao()

    @ApplicationScope
    @Provides
    @Singleton
    fun provideApplicationScope() = CoroutineScope(SupervisorJob())
}