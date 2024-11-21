package com.zzunee.libraryexample.model.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.zzunee.libraryexample.model.db.dao.BookRentalDao
import com.zzunee.libraryexample.model.db.dao.FavoriteBookDao
import com.zzunee.libraryexample.model.util.Converter
import com.zzunee.libraryexample.model.db.entity.SearchHistory
import com.zzunee.libraryexample.model.db.dao.SearchHistoryDao
import com.zzunee.libraryexample.model.db.entity.RentalBook
import com.zzunee.libraryexample.model.db.entity.FavoriteBook

@Database(entities = [SearchHistory::class, FavoriteBook::class, RentalBook::class], version = 1, exportSchema = true)
@TypeConverters(Converter::class)
abstract class LibraryDatabase: RoomDatabase() {
    abstract val historyDao: SearchHistoryDao
    abstract val favoriteBookDao: FavoriteBookDao
    abstract val bookRentalDao: BookRentalDao

    companion object {
        @Volatile
        private var INSTANCE: LibraryDatabase? = null

        fun getDatabase(context: Context): LibraryDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(context, LibraryDatabase::class.java, "Library.db")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also {
                        INSTANCE = it
                    }
            }
        }
    }
}