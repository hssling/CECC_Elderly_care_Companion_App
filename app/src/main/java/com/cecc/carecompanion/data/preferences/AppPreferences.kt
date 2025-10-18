package com.cecc.carecompanion.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "cecc_preferences")

class AppPreferences(private val context: Context) {

    companion object {
        val STAFF_PIN = stringPreferencesKey("staff_pin")
        val BIOMETRIC_ENABLED = booleanPreferencesKey("biometric_enabled")
        val SYNC_ENABLED = booleanPreferencesKey("sync_enabled")
        val LAST_SYNC_TIMESTAMP = longPreferencesKey("last_sync_timestamp")
        val DRIVE_FOLDER_URI = stringPreferencesKey("drive_folder_uri")
        val LANGUAGE = stringPreferencesKey("language")
        val CONSENT_ACCEPTED = booleanPreferencesKey("consent_accepted")
        val OFFLINE_CONTENT_DOWNLOADED = booleanPreferencesKey("offline_content_downloaded")
    }

    val staffPin: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[STAFF_PIN]
    }

    val biometricEnabled: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[BIOMETRIC_ENABLED] ?: false
    }

    val syncEnabled: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[SYNC_ENABLED] ?: false
    }

    val lastSyncTimestamp: Flow<Long> = context.dataStore.data.map { preferences ->
        preferences[LAST_SYNC_TIMESTAMP] ?: 0L
    }

    val driveFolderUri: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[DRIVE_FOLDER_URI]
    }

    val language: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[LANGUAGE] ?: "en"
    }

    val consentAccepted: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[CONSENT_ACCEPTED] ?: false
    }

    val offlineContentDownloaded: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[OFFLINE_CONTENT_DOWNLOADED] ?: false
    }

    suspend fun setStaffPin(pin: String) {
        context.dataStore.edit { preferences ->
            preferences[STAFF_PIN] = pin
        }
    }

    suspend fun setBiometricEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[BIOMETRIC_ENABLED] = enabled
        }
    }

    suspend fun setSyncEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[SYNC_ENABLED] = enabled
        }
    }

    suspend fun setLastSyncTimestamp(timestamp: Long) {
        context.dataStore.edit { preferences ->
            preferences[LAST_SYNC_TIMESTAMP] = timestamp
        }
    }

    suspend fun setDriveFolderUri(uri: String) {
        context.dataStore.edit { preferences ->
            preferences[DRIVE_FOLDER_URI] = uri
        }
    }

    suspend fun setLanguage(language: String) {
        context.dataStore.edit { preferences ->
            preferences[LANGUAGE] = language
        }
    }

    suspend fun setConsentAccepted(accepted: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[CONSENT_ACCEPTED] = accepted
        }
    }

    suspend fun setOfflineContentDownloaded(downloaded: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[OFFLINE_CONTENT_DOWNLOADED] = downloaded
        }
    }
}
