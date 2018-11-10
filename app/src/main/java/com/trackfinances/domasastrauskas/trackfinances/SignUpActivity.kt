package com.trackfinances.domasastrauskas.trackfinances

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast

class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        Toast.makeText(applicationContext, "Currently on development", Toast.LENGTH_LONG).show()

    }

    fun signUp(view: View) {
        //  TODO check if username exists

        // TODO insert user into database
    }
    fun goBack(view: View) {
        val loginActivity = Intent(applicationContext, LoginActivity::class.java)
        startActivity(loginActivity)
        finish()
    }
}
