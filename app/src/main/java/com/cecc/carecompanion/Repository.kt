package com.cecc.carecompanion

import android.content.Context
import androidx.room.Room
import com.cecc.carecompanion.data.local.AppDatabase

object RepositoryProvider {
  @Volatile private var db: AppDatabase? = null
  fun db(ctx: Context): AppDatabase =
    db ?: synchronized(this) {
      db ?: Room.databaseBuilder(ctx, AppDatabase::class.java, "cecc.db").build().also { db = it }
    }
}
