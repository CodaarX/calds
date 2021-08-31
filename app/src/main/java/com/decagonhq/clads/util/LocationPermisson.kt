package com.decagonhq.clads.util

import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.decagonhq.clads.ui.profile.DashboardActivity

object LocationPermission {

    // check for permission
    fun checkPermission(dashboardActivity: DashboardActivity): Boolean {
        return ActivityCompat.checkSelfPermission(dashboardActivity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }
}
