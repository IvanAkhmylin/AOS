package com.vanganistan.aos.Utils

import android.app.Activity
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class CheckPermission {
    companion object{
        fun permissionGranted(activity: Activity, permissions: Array<String>): Boolean {
            for (perm in permissions) {
                val result = ContextCompat.checkSelfPermission(activity, perm)
                if (result != PackageManager.PERMISSION_GRANTED) {
                    return false
                }
            }
            return true
        }

        fun requestPermission(activity: Activity, permissions: Array<String>, code: Int) {
            ActivityCompat.requestPermissions(activity, permissions, code)
        }

    }
}