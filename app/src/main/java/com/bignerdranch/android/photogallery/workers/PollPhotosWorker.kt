package com.bignerdranch.android.photogallery.workers

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import timber.log.Timber

class PollPhotosWorker(context: Context, workerParams: WorkerParameters)
    : Worker(context, workerParams) {

    //region Overrides
    override fun doWork(): Result {
        Timber.d("worker doing work")

        return Result.success()
    }
    //endregion
}