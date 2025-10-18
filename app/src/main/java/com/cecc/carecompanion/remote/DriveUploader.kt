package com.cecc.carecompanion.remote

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import com.cecc.carecompanion.data.local.entities.FormEntry
import com.cecc.carecompanion.data.local.entities.Screening
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import java.io.BufferedWriter
import java.io.OutputStreamWriter
import java.text.SimpleDateFormat
import java.util.*

class DriveUploader(private val context: Context) {

    companion object {
        const val REQUEST_CODE_SELECT_FOLDER = 1001
        private const val PREF_DRIVE_URI = "drive_folder_uri"
    }

    data class UploadResult(
        val isSuccess: Boolean,
        val exception: Exception? = null
    )

    fun getSelectedFolderUri(): Uri? {
        val prefs = context.getSharedPreferences("cecc_prefs", Context.MODE_PRIVATE)
        val uriString = prefs.getString(PREF_DRIVE_URI, null)
        return if (uriString != null) Uri.parse(uriString) else null
    }

    fun setSelectedFolderUri(uri: Uri) {
        val prefs = context.getSharedPreferences("cecc_prefs", Context.MODE_PRIVATE)
        prefs.edit().putString(PREF_DRIVE_URI, uri.toString()).apply()
    }

    fun createSelectFolderIntent(): Intent {
        return Intent(Intent.ACTION_OPEN_DOCUMENT_TREE).apply {
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION or
                    Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
        }
    }

    suspend fun uploadData(
        screenings: List<Screening>,
        forms: List<FormEntry>
    ): UploadResult = withContext(Dispatchers.IO) {
        try {
            val folderUri = getSelectedFolderUri()
                ?: return@withContext UploadResult(false, Exception("No folder selected"))

            val folder = DocumentFile.fromTreeUri(context, folderUri)
                ?: return@withContext UploadResult(false, Exception("Cannot access folder"))

            // Create timestamp for filename
            val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())

            // Upload screenings
            if (screenings.isNotEmpty()) {
                val screeningsData = buildJsonObject {
                    put("screenings", Json.encodeToJsonElement(screenings))
                    put("export_timestamp", System.currentTimeMillis())
                }

                val screeningsFile = folder.createFile("application/json", "screenings_$timestamp.json")
                    ?: return@withContext UploadResult(false, Exception("Cannot create screenings file"))

                context.contentResolver.openOutputStream(screeningsFile.uri)?.use { outputStream ->
                    BufferedWriter(OutputStreamWriter(outputStream)).use { writer ->
                        writer.write(Json { prettyPrint = true }.encodeToString(
                            kotlinx.serialization.json.JsonElement.serializer(),
                            screeningsData
                        ))
                    }
                }
            }

            // Upload forms
            if (forms.isNotEmpty()) {
                val formsData = buildJsonObject {
                    put("forms", Json.encodeToJsonElement(forms))
                    put("export_timestamp", System.currentTimeMillis())
                }

                val formsFile = folder.createFile("application/json", "forms_$timestamp.json")
                    ?: return@withContext UploadResult(false, Exception("Cannot create forms file"))

                context.contentResolver.openOutputStream(formsFile.uri)?.use { outputStream ->
                    BufferedWriter(OutputStreamWriter(outputStream)).use { writer ->
                        writer.write(Json { prettyPrint = true }.encodeToString(
                            kotlinx.serialization.json.JsonElement.serializer(),
                            formsData
                        ))
                    }
                }
            }

            UploadResult(true)
        } catch (e: Exception) {
            UploadResult(false, e)
        }
    }

    fun persistUriPermission(uri: Uri) {
        try {
            context.contentResolver.takePersistableUriPermission(
                uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            )
        } catch (e: SecurityException) {
            // Permission already granted or other security issue
        }
    }
}
