package com.app.spendable.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

interface IAppDatabase {
    fun transactionDao(): TransactionDao
    fun subscriptionDao(): SubscriptionDao
    fun monthDao(): MonthDao
}

object DatabaseConstants {
    const val DATABASE_NAME = "spendable.db"
    const val DATABASE_VERSION = 2
}

@Database(
    entities = [TransactionDBModel::class, SubscriptionDBModel::class, MonthDBModel::class],
    version = DatabaseConstants.DATABASE_VERSION,
)
abstract class AppDatabase : RoomDatabase(), IAppDatabase {

    companion object {
        @Volatile
        private var instance: IAppDatabase? = null

        fun getInstance(context: Context) = instance ?: synchronized(this) {
            Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                DatabaseConstants.DATABASE_NAME
            ).fallbackToDestructiveMigration()
                .build().also { instance = it }
        }
    }

    abstract override fun transactionDao(): TransactionDao
    abstract override fun subscriptionDao(): SubscriptionDao
    abstract override fun monthDao(): MonthDao
}
