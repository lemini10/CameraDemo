package com.example.camerademo

import android.content.pm.PackageManager
import android.icu.text.SimpleDateFormat
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.camerademo.databinding.ActivityMainBinding
import java.io.File
import java.lang.Exception
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var outputDirector: File
    private var imageCapture: ImageCapture? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main)
        outputDirector = getOutputDirectory()
        requestPermission()
    }

    private  fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider= cameraProviderFuture.get()
            val preview = Preview.Builder().build().also {
                mPreview->
                mPreview.setSurfaceProvider(binding.viewFinder.surfaceProvider)
            }
            imageCapture = ImageCapture.Builder().build()
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            try {
                cameraProvider.bindToLifecycle(
                    this,cameraSelector, preview, imageCapture
                )
            } catch (e: Exception) {
                Log.d(Constants.TAG, "Error al inicializar camara")
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun getOutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            mFile->
            File(mFile,"demoClaseCamera").apply {
                mkdirs()
            }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else filesDir
    }

    private fun requestPermission() {
        if (allPermissionsGranted()) {

        } else {
            ActivityCompat.requestPermissions(this,Constants.REQUIRED_PERMISSION,Constants.REQUEST_CODE_PERMISSION)
        }
    }

    private fun allPermissionsGranted() = Constants.REQUIRED_PERMISSION.all {
        ContextCompat.checkSelfPermission(baseContext,it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode==Constants.REQUEST_CODE_PERMISSION) {
            if (allPermissionsGranted()) {

            } else {
                finish()
            }
        } else {
            ActivityCompat.requestPermissions(this,Constants.REQUIRED_PERMISSION,Constants.REQUEST_CODE_PERMISSION)
        }
    }

    private fun takePhoto() {
        val imageCapture = imageCapture?:return
        val photoFile = File(
            outputDirector,
            SimpleDateFormat(Constants.FILE_NAME_FORMAT, Locale.getDefault())
                .format(System.currentTimeMillis()) + ".jpg")

        val outputOption = ImageCapture
            .OutputFileOptions
            .Builder(photoFile)
            .build()

        imageCapture.takePicture(
            outputOption,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.onImageSavedCallback {

            })
    }
}