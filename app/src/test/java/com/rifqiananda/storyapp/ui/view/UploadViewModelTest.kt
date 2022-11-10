package com.rifqiananda.storyapp.ui.view

import android.widget.ImageView
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.rifqiananda.storyapp.data.RemoteRepository
import com.rifqiananda.storyapp.helper.PreferencesHelper
import com.rifqiananda.storyapp.model.FileUploadResponse
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class UploadViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var observer: Observer<FileUploadResponse>

    @Mock
    lateinit var repository: RemoteRepository

    @Mock
    lateinit var sharedPref: PreferencesHelper

    private lateinit var uploadViewModel: UploadViewModel

    private var getImage: ImageView? = null

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        uploadViewModel = UploadViewModel(repository)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun testNullAndObserver() {
        val uploadData = MutableLiveData<FileUploadResponse>()
        uploadData.postValue(FileUploadResponse(false, "Success"))

        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo",
            ""
        )
        val text = ""
        val description = text.toRequestBody("text/plain".toMediaType())

        Mockito.`when`(repository.doUpload("", imageMultipart, description)).thenReturn(uploadData)
        uploadViewModel.upload("", imageMultipart, description).observeForever(observer)

        Assertions.assertNotNull(uploadViewModel.upload("", imageMultipart, description))
        Assertions.assertTrue(uploadViewModel.upload("", imageMultipart, description).hasObservers())
    }
}