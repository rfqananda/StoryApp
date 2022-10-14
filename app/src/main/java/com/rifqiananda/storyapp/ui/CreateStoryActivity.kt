package com.rifqiananda.storyapp.ui

import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.rifqiananda.storyapp.LoadingDialog
import com.rifqiananda.storyapp.databinding.ActivityCreateStoryBinding
import com.rifqiananda.storyapp.helper.Constant
import com.rifqiananda.storyapp.helper.PreferencesHelper
import com.rifqiananda.storyapp.model.FileUploadResponse
import com.rifqiananda.storyapp.networking.ApiRetrofit
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import kotlin.properties.Delegates

class CreateStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateStoryBinding

    private val api by lazy { ApiRetrofit().endpoint }

    lateinit var sharedPref: PreferencesHelper

    private lateinit var loading: LoadingDialog

    private var isBackCamera by Delegates.notNull<Boolean>()

    companion object {
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_PERMISSIONS = arrayOf(android.Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                showMessage("Tidak mendapatkan permission.")
                finish()
            }
        }
    }
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loading = LoadingDialog(this)

        sharedPref = PreferencesHelper(this)

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        binding.cameraXButton.setOnClickListener { startCameraX() }
        binding.uploadButton.setOnClickListener {
            loading.startLoading()
            uploadImage()
        }
        binding.galleryButton.setOnClickListener { startGallery() }
    }


    private fun startCameraX() {
        val intent = Intent(this, CameraXActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private var getFile: File? = null
    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {

            val myFile = it.data?.getSerializableExtra("picture") as File
            isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean

            val result = rotateBitmap(
                BitmapFactory.decodeFile(myFile.path),
                isBackCamera
            )
            binding.previewImageView.setImageBitmap(result)

            getFile = reduceFileImage(myFile, isBackCamera)
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this)
            getFile = myFile
            binding.previewImageView.setImageURI(selectedImg)
        }
    }

    private fun uploadImage() {

        if (getFile != null) {

            if(binding.etDesc.text.toString().isEmpty()){
                loading.isDismiss()
                showMessage("Isi Deskripsi terlebih dahulu")

            }
            else {

                val file = getFile as File

                val description = binding.etDesc.text.toString().toRequestBody("text/plain".toMediaType())
                val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                    "photo",
                    file.name,
                    requestImageFile
                )
                val token = sharedPref.getString(Constant.PREF_TOKEN)
                val fixToken = "Bearer $token"

                api.uploadImage(fixToken, imageMultipart, description).enqueue(object : Callback<FileUploadResponse> {
                    override fun onResponse(
                        call: Call<FileUploadResponse>,
                        response: Response<FileUploadResponse>
                    ) {
                        if (response.isSuccessful) {
                            loading.isDismiss()
                            val responseBody = response.body()
                            if (responseBody != null && !responseBody.error) {
                                showMessage(responseBody.message)
                                finish()
                            }
                        } else {
                            loading.isDismiss()
                            showMessage(response.message())
                        }

                    }

                    override fun onFailure(call: Call<FileUploadResponse>, t: Throwable) {
                        loading.isDismiss()
                        showMessage("Gagal instance Retrofit")
                    }
                })
            }

        } else {
            loading.isDismiss()
            showMessage("Silakan masukkan berkas gambar terlebih dahulu.")
        }
    }

    private fun reduceFileImage(file: File, isBackCamera: Boolean): File {
        var bitmap = BitmapFactory.decodeFile(file.path)
        bitmap = rotateBitmap(bitmap, isBackCamera)
        var compressQuality = 100
        var streamLength: Int
        do {
            val bmpStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
            val bmpPicByteArray = bmpStream.toByteArray()
            streamLength = bmpPicByteArray.size
            compressQuality -= 5
        } while (streamLength > 1000000)
        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))
        return file
    }

    private fun showMessage(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

}