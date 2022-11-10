package com.rifqiananda.storyapp.ui.view

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.rifqiananda.storyapp.data.RemoteRepository
import com.rifqiananda.storyapp.model.Register
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
class RegisterViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var observer: Observer<Register>

    @Mock
    lateinit var repository: RemoteRepository

    private lateinit var registerViewModel: RegisterViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        registerViewModel = RegisterViewModel(repository)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun testNullAndObserver() {
        val registerData = MutableLiveData<Register>()
        registerData.postValue(Register(false, "Success"))

        Mockito.`when`(repository.doRegister("", "", "")).thenReturn(registerData)
        registerViewModel.register("","","").observeForever(observer)
        Assertions.assertNotNull(registerViewModel.register("","",""))
        Assertions.assertTrue(registerViewModel.register("","","").hasObservers())
    }

    @Test
    fun register() {
        val registerData = MutableLiveData<Register>()
        registerData.postValue(Register(false,  "Success"))

        Mockito.`when`(repository.doRegister("raka riki", "rakariki@gmail.com", "coba123")).thenReturn(registerData)
        registerViewModel.register("raka riki", "rakariki@gmail.com", "coba123").observeForever {
            Assertions.assertEquals(it.error, false)
            Assertions.assertTrue(it is Register)
        }
    }
}