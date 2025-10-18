package com.cecc.carecompanion.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class Screening(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val participantId: String,
  val type: String,
  val score: Int,
  val risk: String,
  val createdAt: Long
)

@Entity
@Serializable
data class FormEntry(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val participantId: String,
  val formType: String, // baseline/midline/endline
  val dataJson: String,
  val createdAt: Long
)
