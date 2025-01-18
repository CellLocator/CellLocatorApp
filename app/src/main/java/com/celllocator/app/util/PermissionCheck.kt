package com.celllocator.app.util

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

fun checkAndRequestPermissions(context: Context, currentActivity: Activity): Boolean {

    val notGranted = permissionsArray.filter {
        ContextCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED
    }

    if (notGranted.isNotEmpty()) {
        ActivityCompat.requestPermissions(currentActivity, notGranted.toTypedArray(), 0)
    }

    return notGranted.isEmpty()
}