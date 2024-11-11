package com.zzunee.shoppingexample.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.zzunee.shoppingexample.data.db.dao.BookRentalDao
import com.zzunee.shoppingexample.data.db.dao.FavoriteBookDao
import com.zzunee.shoppingexample.data.util.Converter
import com.zzunee.shoppingexample.data.db.entity.SearchHistory
import com.zzunee.shoppingexample.data.db.dao.SearchHistoryDao
import com.zzunee.shoppingexample.data.db.entity.RentalBook
import com.zzunee.shoppingexample.data.db.entity.FavoriteBook

@Database(entities = [SearchHistory::class, FavoriteBook::class, RentalBook::class], version = 2, exportSchema = true)
@TypeConverters(Converter::class)
abstract class ShoppingDatabase: RoomDatabase() {
    abstract val historyDao: SearchHistoryDao
    abstract val favoriteBookDao: FavoriteBookDao
    abstract val bookRentalDao: BookRentalDao

    companion object {
        @Volatile
        private var INSTANCE: ShoppingDatabase? = null

        fun getDatabase(context: Context): ShoppingDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(context, ShoppingDatabase::class.java, "Shopping.db")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also {
                        INSTANCE = it
                    }
            }
        }
    }
}