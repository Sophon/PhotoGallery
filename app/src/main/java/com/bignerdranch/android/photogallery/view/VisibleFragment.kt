package com.bignerdranch.android.photogallery.view

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.bignerdranch.android.photogallery.workers.PollPhotosWorker
import timber.log.Timber

abstract class VisibleFragment: Fragment() {

    private val onNotificationReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Timber.i("Notification: intercepted; canceling")

            resultCode = Activity.RESULT_CANCELED
        }
    }

    //region Lifecycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireContext().registerReceiver(
            onNotificationReceiver,
            IntentFilter(PollPhotosWorker.ACTION_SHOW_NOTIFICATION),
            PollPhotosWorker.PERM_PRIVATE,
            null
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()

        requireContext().unregisterReceiver(onNotificationReceiver)
    }
}