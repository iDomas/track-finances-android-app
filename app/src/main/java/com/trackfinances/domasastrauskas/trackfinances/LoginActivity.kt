package com.trackfinances.domasastrauskas.trackfinances

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.Toast
import com.android.volley.NetworkResponse
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.trackfinances.domasastrauskas.trackfinances.globals.AuthToken
import com.trackfinances.domasastrauskas.trackfinances.model.UserAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    var globalAuthToken = AuthToken.Token

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.INTERNET
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(applicationContext, "NO access to internet.", Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.INTERNET), 1)
        } else {
            Toast.makeText(applicationContext, "Access to internet granted.", Toast.LENGTH_LONG).show();
        }
    }



    fun login(view: View) {
        val user = UserAuth(
            textUsername.text.toString(),
            textPassword.text.toString()
        )

        login(user)
    }

    fun startSignUp(view: View) {
        val signUpActivity = Intent(applicationContext, SignUpActivity::class.java)
        startActivity(signUpActivity)
        finish()
    }

    private fun login(user: UserAuth) {
        val authUrl = getString(R.string.backendUrl) + "/login"
        val queue = Volley.newRequestQueue(applicationContext)

        val stringRequest = object : StringRequest(Request.Method.POST, authUrl,
                Response.Listener<String> { response ->
                    println("Login resp: $response")
                },
                Response.ErrorListener { error ->
                    Toast.makeText(applicationContext, "Wrong credentials!", Toast.LENGTH_LONG).show()
                    println("Login failure: $error")
                }
            ) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                val userToJson = Gson().toJson(user)
                return userToJson.toString().toByteArray()
            }

            override fun parseNetworkResponse(response: NetworkResponse?): Response<String> {
                println("Auth token: ${response?.headers?.get("Authorization")}")
                if (response?.headers?.get("Authorization") != null && response?.statusCode == 200) {
                    globalAuthToken.authToken = response?.headers?.get("Authorization")
                    val dashboardActivity = Intent(applicationContext, DashboardActivity::class.java)
                    startActivity(dashboardActivity)
                    finish()
                }
                return super.parseNetworkResponse(response)
            }
        }

        queue.add(stringRequest)
    }
}
