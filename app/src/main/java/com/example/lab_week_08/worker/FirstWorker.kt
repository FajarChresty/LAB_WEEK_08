package com.example.lab_week_08.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class FirstWorker(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {

    companion object {
        const val INPUT_DATA_ID = "input_id"
    }

    override fun doWork(): Result {
        NotificationUtils.showNotification(
            applicationContext,
            "WorkManager Status",
            "FirstWorker completed!",
            1
        )
        return Result.success()
    }
}
