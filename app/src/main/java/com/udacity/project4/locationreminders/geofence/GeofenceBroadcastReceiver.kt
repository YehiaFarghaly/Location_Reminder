package com.udacity.project4.locationreminders.geofence

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent
import com.udacity.project4.R

class GeofenceBroadcastReceiver : BroadcastReceiver() {
    companion object {
        internal const val ACTION_GEOFENCE_EVENT =
            "SaveReminderFragment.LocationReminder.action.ACTION_GEOFENCE_EVENT"
    }
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == ACTION_GEOFENCE_EVENT) {
            GeofenceTransitionsJobIntentService.enqueueWork(context, intent)
            val geofencingEvent = GeofencingEvent.fromIntent(intent)
            if (geofencingEvent.geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
                val Id = when {
                    geofencingEvent.triggeringGeofences.isNotEmpty() ->
                        geofencingEvent.triggeringGeofences[0].requestId
                    else -> {
                        return
                    }
                }
                val idx = GeofencingConstants.LANDMARK_DATA.indexOfFirst {
                    it.id == Id
                }
                if ( -1 == idx ) {
                    return
                }
                val notificationManager = ContextCompat.getSystemService(
                    context,
                    NotificationManager::class.java
                ) as NotificationManager

                notificationManager.sendGeofenceNotification(
                    context, idx
                )
            }
        }
    }
}