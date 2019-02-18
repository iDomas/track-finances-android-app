package com.trackfinances.domasastrauskas.trackfinances

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity(), TextWatcher {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        Toast.makeText(applicationContext, "Currently on development", Toast.LENGTH_LONG).show()

        enterUsername.addTextChangedListener(this)
        enterPassword.addTextChangedListener(this)
        retypePassword.addTextChangedListener(this)

    }

    override fun afterTextChanged(s: Editable?) {
        Toast.makeText(applicationContext, "after text changed", Toast.LENGTH_SHORT).show()
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        Toast.makeText(applicationContext, "before text changed", Toast.LENGTH_SHORT).show()

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        Toast.makeText(applicationContext, "on text changed", Toast.LENGTH_SHORT).show()
    }


    fun signUp(view: View) {
        // TODO validate password

        //  TODO check if username exists

        // TODO insert user into database
    }
    fun goBack(view: View) {
        val loginActivity = Intent(applicationContext, LoginActivity::class.java)
        startActivity(loginActivity)
        finish()
    }
}
