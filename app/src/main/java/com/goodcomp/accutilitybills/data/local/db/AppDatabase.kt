package com.goodcomp.accutilitybills.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.goodcomp.accutilitybills.data.local.dao.CommunalPaymentNoteDao
import com.goodcomp.accutilitybills.data.local.dao.PaymentTypeDao
import com.goodcomp.accutilitybills.data.local.dao.StatisticDao
import com.goodcomp.accutilitybills.data.local.entity.CommunalPaymentNoteEntity
import com.goodcomp.accutilitybills.data.local.entity.PaymentTypeEntity
import com.goodcomp.accutilitybills.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(
    entities = [
        CommunalPaymentNoteEntity::class,
        PaymentTypeEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun communalPaymentNoteDao(): CommunalPaymentNoteDao
    abstract fun paymentTypeDao(): PaymentTypeDao
    abstract fun statisticDao(): StatisticDao

    class Callback @Inject constructor(
        private val database: Provider<AppDatabase>,
        @ApplicationScope private val applicationScope: CoroutineScope,
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            val paymentTypeDao = database.get().paymentTypeDao()

            applicationScope.launch {
                paymentTypeDao.insertPaymentType(
                    PaymentTypeEntity(
                        id = 0,
                        name = "Газ",
                        counterAvailable = true
                    )
                )
                paymentTypeDao.insertPaymentType(
                    PaymentTypeEntity(
                        id = 0,
                        name = "Электричество",
                        counterAvailable = true
                    )
                )
                paymentTypeDao.insertPaymentType(
                    PaymentTypeEntity(
                        id = 0,
                        name = "Вода холодная",
                        counterAvailable = true
                    )
                )
                paymentTypeDao.insertPaymentType(
                    PaymentTypeEntity(
                        id = 0,
                        name = "Вода горячая",
                        counterAvailable = true
                    )
                )
                paymentTypeDao.insertPaymentType(
                    PaymentTypeEntity(
                        id = 0,
                        name = "Кап.ремонт",
                        counterAvailable = false
                    )
                )
                paymentTypeDao.insertPaymentType(
                    PaymentTypeEntity(
                        id = 0,
                        name = "Содержание жилья",
                        counterAvailable = false
                    )
                )
                paymentTypeDao.insertPaymentType(
                    PaymentTypeEntity(
                        id = 0,
                        name = "Мусор",
                        counterAvailable = false
                    )
                )
            }
        }
    }

    companion object {
        const val DATABASE_NAME = "accounting_for_utility_bills_database.db"
    }
}