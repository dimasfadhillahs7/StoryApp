package com.dimasfs.storyappsubmission2.ui.create

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.dimasfs.storyappsubmission2.R
import com.dimasfs.storyappsubmission2.databinding.ActivityCreateBinding
import com.dimasfs.storyappsubmission2.extensions.animateVisibility
import com.dimasfs.storyappsubmission2.extensions.createCustomTempFile
import com.dimasfs.storyappsubmission2.extensions.reduceFileImage
import com.dimasfs.storyappsubmission2.extensions.uriToFile
import com.dimasfs.storyappsubmission2.ui.ViewModelFactory
import com.dimasfs.storyappsubmission2.ui.main.MainActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class CreateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateBinding
    private lateinit var createViewModel: CreateViewModel
    private lateinit var factory: ViewModelFactory
    private lateinit var currentPhotoPath: String
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var getFile: File? = null
    private var location: Location? = null
    private var lat: RequestBody? = null
    private var lon: RequestBody? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        factory = ViewModelFactory.getInstance(this)
        createViewModel =ViewModelProvider(this, factory)[CreateViewModel::class.java]
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        binding.buttonCamera.setOnClickListener { startTakePhoto() }
        binding.buttonGallery.setOnClickListener { startGallery() }
        binding.switchLocation.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                getLocation()
            } else {
                this.location = null
            }
        }

        clickSave()

    }

    private fun clickSave() {
        binding.buttonSave.setOnClickListener{
            createStory()
        }
    }

    private fun createStory() {
        createViewModel.getUser().observe(this@CreateActivity) {
            val token = "Bearer " + it.token
            if (getFile != null) {
                val file = reduceFileImage(getFile as File)
                val description = "${binding.etDescription.text}".toRequestBody("text/plain".toMediaType())
                val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val imageMultipart: MultipartBody.Part =
                    MultipartBody.Part.createFormData("photo", file.name, requestImageFile)

                createViewModel.createStory(token, imageMultipart, description, lat, lon).observe(this) { result ->
                        if (result.isSuccess) {
                            runLoading(true)
                            val mainIntent = Intent(this@CreateActivity, MainActivity::class.java).apply {
                                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                            }
                            startActivity(mainIntent)
                            Toast.makeText(this, getString(R.string.upload_successful), Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            Toast.makeText(this, getString(R.string.upload_failed), Toast.LENGTH_LONG).show()
                        }
                    }
            }

            else {
                Toast.makeText(this@CreateActivity, getString(R.string.upload_your_photo), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)
            val result = BitmapFactory.decodeFile(myFile.path)
            binding.imageView.setImageBitmap(result)
            getFile = myFile
        }
    }


    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)
        createCustomTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this@CreateActivity,
                "com.dimasfs.storyappsubmission2.temp",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }


    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this@CreateActivity)
            binding.imageView.setImageURI(selectedImg)
            getFile = myFile
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val requestLocationPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getLocation()
            }
        }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext, Manifest.permission
                    .ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener {
                if (it != null) {
                    this.location = it
                    lat = it.latitude.toString().toRequestBody("text/plain".toMediaType())
                    lon = it.longitude.toString().toRequestBody("text/plain".toMediaType())
                } else {
                    Toast.makeText(this, getString(R.string.activate_your_location), Toast.LENGTH_SHORT).show()
                    binding.switchLocation.isChecked = false
                }
            }
        }
        else {
            requestLocationPermissionLauncher.launch(
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            (Manifest.permission.ACCESS_COARSE_LOCATION)

        }
    }

    private fun runLoading(b: Boolean) {
        binding.apply {
            imageView.isEnabled = !b
            buttonSave.isEnabled = !b

            if (b) {
                loading.animateVisibility(true)
            } else {
                loading.animateVisibility(false)
            }
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}