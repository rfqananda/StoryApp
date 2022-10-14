package com.rifqiananda.storyapp.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.rifqiananda.storyapp.LoadingDialog
import com.rifqiananda.storyapp.R
import com.rifqiananda.storyapp.databinding.ActivityLoginBinding
import com.rifqiananda.storyapp.helper.Constant
import com.rifqiananda.storyapp.helper.PreferencesHelper
import com.rifqiananda.storyapp.model.Login
import com.rifqiananda.storyapp.networking.ApiRetrofit
import com.rifqiananda.storyapp.ui.custom.MyButton
import com.rifqiananda.storyapp.ui.custom.MyEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : AppCompatActivity() {

    lateinit var sharedPref: PreferencesHelper

    private val api by lazy { ApiRetrofit().endpoint }

    private lateinit var binding: ActivityLoginBinding

    private lateinit var loading: LoadingDialog

    private lateinit var myEditText: MyEditText

    private lateinit var myButton: MyButton

    private var email: String = ""
    private var pass: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        myButton = findViewById(R.id.btn_login)
        myEditText = findViewById(R.id.et_password)

        loading = LoadingDialog(this)

        sharedPref = PreferencesHelper(this)

        setMyButtonEnable()

        myEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setMyButtonEnable()
            }
            override fun afterTextChanged(s: Editable) {
            }
        })

        binding.apply {

            etEmail.doOnTextChanged { text, start, before, count ->
                if (Patterns.EMAIL_ADDRESS.matcher(etEmail.text.toString()).matches()){
                    input1.error = null
                    input1.isErrorEnabled = false
                } else input1.error = "The email you are using is not correct!"
            }

            etPassword.doOnTextChanged { text, start, before, count ->
                if (text!!.length < 6) {
                    input2.error = "Your password is less than 6 characters!"
                } else input2.error = null
            }

            etPassword.setOnKeyListener { v, keyCode, event ->

                email = etEmail.text.toString()
                pass = etPassword.text.toString()

                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER){
                    loginUser(email, pass)
                    closeKeyboard(etPassword)
                    loading.startLoading()
                    return@setOnKeyListener true
                }
                return@setOnKeyListener false
            }

            btnSignUp.setOnClickListener {
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
                anim()
            }

            btnLogin.setOnClickListener {
                closeKeyboard(btnLogin)
                email = etEmail.text.toString()
                pass = etPassword.text.toString()

                if (email.isEmpty()){
                    input1.error = "Email cannot be empty!!"
                }
                else if (pass.isEmpty()){
                    input2.error = "Password cannot be empty!"
                }
                else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    input1.error = "The email you are using is not correct!"
                }
                else {
                    loading.startLoading()
                    loginUser(email, pass)
                }

            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (sharedPref.getBoolean(Constant.PREF_IS_LOGIN)) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun loginUser(email: String, password: String) {
        api.login(email, password).enqueue(object : Callback<Login> {

            override fun onResponse(call: Call<Login>, response: Response<Login>) {
                if (response.isSuccessful) {
                    if (response.body()?.error == false) {
                        loading.isDismiss()

                        val body = response.body()!!
                        val token = body.loginResult.token
                        val name = body.loginResult.name

                        saveSession(token, name)

                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                        //ini nanti dihapus
                        showMessage("Login succeed!")
                    }
                } else {
                    loading.isDismiss()
                    showMessage(getString(R.string.login_failed))
                }
            }

            override fun onFailure(call: Call<Login>, t: Throwable) {
                Log.e("Login Error:", "${t.message}")
            }
        })
    }

    private fun saveSession(token: String, name: String) {
        sharedPref.put(Constant.PREF_IS_LOGIN, true)
        sharedPref.put(Constant.PREF_NAME, name)
        sharedPref.put(Constant.PREF_TOKEN, token)
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun anim() {
        overridePendingTransition(R.anim.login_to_register_enter, R.anim.login_to_register_exit)
    }

    private fun closeKeyboard(view: View){
        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun setMyButtonEnable() {
        val result = myEditText.text
        myButton.isEnabled = result != null && result.toString().isNotEmpty()
    }
}