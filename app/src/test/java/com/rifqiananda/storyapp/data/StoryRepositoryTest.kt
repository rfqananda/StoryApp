package com.rifqiananda.storyapp.data

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.rifqiananda.storyapp.MainDispatcherRule
import com.rifqiananda.storyapp.helper.Constant
import com.rifqiananda.storyapp.helper.PreferencesHelper
import com.rifqiananda.storyapp.networking.ApiEndPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.Extensions
import org.mockito.Mockito.mock
import org.mockito.junit.jupiter.MockitoExtension


@ExperimentalCoroutinesApi
@Extensions(value = [
    ExtendWith(MockitoExtension::class)
])
class StoryRepositoryTest{

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    lateinit var apiService: ApiEndPoint
    lateinit var sharedPref: PreferencesHelper


    @Before
    fun setUp() {
        apiService = FakeApiService()
    }

    @Test
    fun `when getData Should Not Null`() = runTest {
        sharedPref = PreferencesHelper(mock(Context::class.java))
        val token = sharedPref.getString(Constant.PREF_TOKEN)
        val bearer = "Bearer $token"

        val actualNews = apiService.getAllStories(bearer, 1,1)
        Assert.assertNotNull(actualNews)
    }

}