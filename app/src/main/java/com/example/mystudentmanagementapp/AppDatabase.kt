package com.example.mystudentmanagementapp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Student::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun studentDao(): StudentDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "student_database"
                )
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries() // ✅ Thêm dòng này để tránh lỗi truy cập DB trên Main Thread
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
