package com.udacity.project4.locationreminders.geofence

import android.content.Context
import android.content.Intent
import androidx.core.app.JobIntentService
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent
import com.udacity.project4.locationreminders.data.ReminderDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import com.udacity.project4.locationreminders.data.local.RemindersLocalRepository
import com.udacity.project4.locationreminders.reminderslist.ReminderDataItem
import com.udacity.project4.utils.sendNotification
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject
import kotlin.coroutines.CoroutineContext

class GeofenceTransitionsJobIntentService : JobIntentService(), CoroutineScope {
    override val coroutineContext: CoroutineContext = Dispatchers.IO + Job()

    override fun onHandleWork(intent: Intent) {
        val geofenceRequest = GeofencingEvent.fromIntent(intent)
        if (geofenceRequest.geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
            if (geofenceRequest.triggeringGeofences.isNotEmpty()) {
                sendNotification(geofenceRequest.triggeringGeofences)
            }
        }
    }

    companion object {
        private const val JOB_ID = 100
        fun enqueueWork(context: Context, intent: Intent) {
            enqueueWork(
                context,
                GeofenceTransitionsJobIntentService::class.java, JOB_ID,
                intent
            )
        }
    }
        private fun sendNotification(triggeringGeofences: List<Geofence>) {
            for (triggeringGeofence in triggeringGeofences) {
                val ID = triggeringGeofence.requestId
                val remindersLocalRepository: ReminderDataSource by inject()
                CoroutineScope(coroutineContext).launch(SupervisorJob()) {
                    val result = remindersLocalRepository.getReminder(ID)
                    if (result is Result.Success<ReminderDTO>) {
                        val reminderDTO = result.data
                        sendNotification(
                            this@GeofenceTransitionsJobIntentService, ReminderDataItem(
                                reminderDTO.title,
                                reminderDTO.description,
                                reminderDTO.location,
                                reminderDTO.latitude,
                                reminderDTO.longitude,
                                reminderDTO.id
                            )
                        )
                    }
                }
            }
        }


}
