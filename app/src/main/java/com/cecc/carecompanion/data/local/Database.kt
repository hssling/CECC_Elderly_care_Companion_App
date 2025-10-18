package com.cecc.carecompanion.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cecc.carecompanion.data.local.dao.*
import com.cecc.carecompanion.data.local.entities.*

@Database(entities = [Screening::class, FormEntry::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
  abstract fun screeningDao(): ScreeningDao
  abstract fun formDao(): FormDao
}
