package com.cecc.carecompanion.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.cecc.carecompanion.RepositoryProvider
import com.cecc.carecompanion.remote.DriveUploader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SyncWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val context = applicationContext

            // Get all unsynced data
            val db = RepositoryProvider.db(context)
            val allScreenings = db.screeningDao().getAllScreenings()
            val allForms = db.formDao().getAllForms()

            // Upload to Drive
            val uploader = DriveUploader(context)
            val uploadResult = uploader.uploadData(allScreenings, allForms)

            if (uploadResult.isSuccess) {
                // Mark data as synced (you might want to add a synced flag to entities)
                val resultData = workDataOf("success" to true, "message" to "Sync completed successfully")
                Result.success(resultData)
            } else {
                val resultData = workDataOf("success" to false, "message" to uploadResult.exception?.message)
                Result.failure(resultData)
            }
        } catch (e: Exception) {
            val resultData = workDataOf("success" to false, "message" to e.message)
            Result.failure(resultData)
        }
    }
}
