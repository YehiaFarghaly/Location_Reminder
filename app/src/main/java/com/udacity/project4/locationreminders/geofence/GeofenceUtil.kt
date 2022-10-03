package com.udacity.project4.locationreminders.geofence

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import com.udacity.project4.MyApp
import com.udacity.project4.R
import com.google.android.gms.maps.model.LatLng


private const val NOTIFICATION_ID = 200
private const val CHANNEL_ID = "geofence"
data class LandmarkDataObject(val id: String, val hint: Int, val name: Int, val latLong: LatLng)

internal object GeofencingConstants {
    val LANDMARK_DATA = arrayOf(
        LandmarkDataObject(
            "golden_gate_bridge",
            R.string.golden_gate_bridge_hint,
            R.string.golden_gate_bridge_location,
            LatLng(37.819927, -122.478256)),

        LandmarkDataObject(
            "ferry_building",
            R.string.ferry_building_hint,
            R.string.ferry_building_location,
            LatLng(37.795490, -122.394276)),

        LandmarkDataObject(
            "pier_39",
            R.string.pier_39_hint,
            R.string.pier_39_location,
            LatLng(37.808674, -122.409821)),

        LandmarkDataObject(
            "union_square",
            R.string.union_square_hint,
            R.string.union_square_location,
            LatLng(37.788151, -122.407570))
    )
    const val EXTRA_GEOFENCE_INDEX = "GEOFENCE_INDEX"
}

fun NotificationManager.sendGeofenceNotification(context: Context, foundIndex: Int) {
    val contentIntent = Intent(context, MyApp::class.java)
    contentIntent.putExtra(GeofencingConstants.EXTRA_GEOFENCE_INDEX, foundIndex)
    val contentPendingIntent = PendingIntent.getActivity(
        context,
        NOTIFICATION_ID,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )
    val icon = BitmapFactory.decodeResource(
        context.resources,
        R.drawable.map_icon
    )
    val bigPictureStyle = NotificationCompat.BigPictureStyle()
        .bigPicture(icon)
        .bigLargeIcon(null)

    val builder = NotificationCompat.Builder(context, CHANNEL_ID)
        .setContentTitle(context.getString(R.string.app_name))
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setSmallIcon(R.drawable.map_icon)
        .setLargeIcon(icon)
        .setContentIntent(contentPendingIntent)
        .setStyle(bigPictureStyle)
        .setAutoCancel(true)


    notify(NOTIFICATION_ID, builder.build())
}

