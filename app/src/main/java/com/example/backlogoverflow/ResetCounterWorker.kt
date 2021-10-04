package com.example.backlogoverflow

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class ResetCounterWorker(private val appContext: Context, workerParams: WorkerParameters): Worker(appContext, workerParams) {

    override fun doWork(): Result {
        appContext.getSharedPreferences("PREFERENCES", Context.MODE_PRIVATE)
            .edit()
            .putInt("count", 0)
            .apply()

        return Result.success()
    }

}
