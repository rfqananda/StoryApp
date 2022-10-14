package com.rifqiananda.storyapp.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.widget.EditText
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.addTextChangedListener
import com.google.android.material.textfield.TextInputLayout
import com.rifqiananda.storyapp.R


class MyEditText : AppCompatEditText {

    private lateinit var editText: EditText

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init() }


    private fun init() {

        editText = findViewById(R.id.et_password)

        editText.addTextChangedListener {
            if (text!!.length < 6) {
                editText.error = "Your password is less than 6 characters!"
            } else editText.error = null
        }
    }
}