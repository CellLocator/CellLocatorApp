package com.celllocator.app.util

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

fun checkPermissions(context: Context): Boolean {
    val notGranted = permissionsArray.filter {
        ContextCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED
    }

    return notGranted.isEmpty()
}

fun requestPermissions(currentActivity: Activity) {
    ActivityCompat.requestPermissions(currentActivity, permissionsArray, 0)
}