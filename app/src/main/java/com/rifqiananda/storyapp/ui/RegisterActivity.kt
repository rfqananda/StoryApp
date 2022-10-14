package com.rifqiananda.storyapp.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.rifqiananda.storyapp.LoadingDialog
import com.rifqiananda.storyapp.R
import com.rifqiananda.storyapp.databinding.ActivityRegisterBinding
import com.rifqiananda.storyapp.helper.PreferencesHelper
import com.rifqiananda.storyapp.model.Submit
import com.rifqiananda.storyapp.networking.ApiRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    private val api by lazy { ApiRetrofit().endpoint }

    lateinit var sharedPref: PreferencesHelper

    private var name: String = ""
    private var email: String = ""
    private var pass: String = ""

    private lateinit var binding: ActivityRegisterBinding

    private lateinit var loading: LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loading = LoadingDialog(this)

        sharedPref = PreferencesHelper(this)

        binding.apply {

            etName.doOnTextChanged { text, start, before, count ->
                if (etName.text.toString().isNotEmpty()){
                    input1.error = null
                    input1.isErrorEnabled = false
                }
                else input1.error = "Name cannot be empty!"
            }

            etEmail.doOnTextChanged { text, start, before, count ->
                if (Patterns.EMAIL_ADDRESS.matcher(etEmail.text.toString()).matches()){
                    input2.error = null
                    input2.isErrorEnabled = false
                } else input2.error = "The registered email must be valid!"
            }

            btnRegister.setOnClickListener {
                closeKeyboard(btnRegister)
                name = etName.text.toString()
                email = etEmail.text.toString()
                pass = etPassword.text.toString()

                if (name.isEmpty()){
                    input1.error = "Name cannot be empty!"
                }
                else if (email.isEmpty()){
                    input2.error = "Email cannot be empty!"
                }
                else if (pass.isEmpty()){
                    input3.error = "Password cannot be empty!"
                }
                else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    input2.error = "The email you are using is not correct!"
                }
                else {
                    loading.startLoading()
                    userRegister(name, email, pass)
                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        anim()
        finish()

    }

    private fun userRegister(name: String, email: String, password: String){
        api.createAccount(name, email, password).enqueue(object : Callback<Submit> {
            override fun onResponse(call: Call<Submit>, response: Response<Submit>
            ){
                if (response.isSuccessful) {

                    loading.isDismiss()
                    val submit = response.body()
                    // ini nanti diganti
                    showMessage(submit!!.message)

                    val i = Intent(this@RegisterActivity, LoginActivity::class.java)
                    startActivity(i)
                    anim()
                    finish()
                }
                else {
                    loading.isDismiss()
                    showMessage(getString(R.string.email_taken))
                }
            }
            override fun onFailure(call: Call<Submit>, t: Throwable) {
                loading.isDismiss()
                Log.e("Login Error:", "${t.message}")
            }
        })
    }

    private fun anim() {
        overridePendingTransition(R.anim.register_to_login_enter, R.anim.register_to_login_exit)
    }

    private fun showMessage(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    private fun closeKeyboard(view: View){
        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}