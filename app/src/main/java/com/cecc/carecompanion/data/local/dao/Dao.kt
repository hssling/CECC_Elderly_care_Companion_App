package com.cecc.carecompanion.data.local.dao

import androidx.room.*
import com.cecc.carecompanion.data.local.entities.FormEntry
import com.cecc.carecompanion.data.local.entities.Screening
import kotlinx.coroutines.flow.Flow

@Dao
interface ScreeningDao {
  @Insert suspend fun insert(item: Screening)
  @Query("SELECT * FROM Screening WHERE participantId = :pid ORDER BY createdAt DESC")
  fun byParticipant(pid: String): Flow<List<Screening>>
  @Query("SELECT * FROM Screening ORDER BY createdAt DESC")
  @JvmSuppressWildcards
  suspend fun getAllScreenings(): List<Screening>
}

@Dao
interface FormDao {
  @Insert suspend fun insert(item: FormEntry)
  @Query("SELECT * FROM FormEntry WHERE participantId = :pid AND formType = :ft ORDER BY createdAt DESC LIMIT 1")
  suspend fun latestByType(pid: String, ft: String): FormEntry?
  @Query("SELECT * FROM FormEntry ORDER BY createdAt DESC")
  @JvmSuppressWildcards
  suspend fun getAllForms(): List<FormEntry>
}
