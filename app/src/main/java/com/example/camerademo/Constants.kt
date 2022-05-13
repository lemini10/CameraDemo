package com.example.camerademo

import android.Manifest

object Constants {
    const val TAG = "Demo Camara"
    const val FILE_NAME_FORMAT = "yy-MM-dd-HH-mm-ss-SSS"
    const val REQUEST_CODE_PERMISSION = 123
    val REQUIRED_PERMISSION = arrayOf(Manifest.permission.CAMERA)
}