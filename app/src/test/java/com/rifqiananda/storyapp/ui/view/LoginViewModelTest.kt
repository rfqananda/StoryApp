package com.rifqiananda.storyapp.ui.view

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.rifqiananda.storyapp.data.RemoteRepository
import com.rifqiananda.storyapp.model.Login
import com.rifqiananda.storyapp.model.LoginResult
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
class LoginViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var observer: Observer<Login>

    @Mock
    lateinit var repository: RemoteRepository

    private lateinit var loginViewModel: LoginViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        loginViewModel = LoginViewModel(repository)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun testNullAndObserver() {
        val loginData = MutableLiveData<Login>()
        loginData.postValue(Login(false, LoginResult("abc", "abc123", "1"), "Success"))

        Mockito.`when`(repository.doLogin("", "")).thenReturn(loginData)
        loginViewModel.login("", "").observeForever(observer)
        Assertions.assertNotNull(loginViewModel.login("", ""))
        Assertions.assertTrue(loginViewModel.login("", "").hasObservers())
    }

    @Test
    fun login() {
        val loginData = MutableLiveData<Login>()
        loginData.postValue(Login(false, LoginResult("Bambang", "abc123", "1"), "Success"))

        Mockito.`when`(repository.doLogin("bambang@gmail.com", "bam123")).thenReturn(loginData)
        loginViewModel.login("bambang@gmail.com", "bam123").observeForever {
            Assertions.assertEquals(it.error, false)
            Assertions.assertNotNull(it.loginResult.name, "Bambang")
            Assertions.assertTrue(it is Login)
        }
    }
}