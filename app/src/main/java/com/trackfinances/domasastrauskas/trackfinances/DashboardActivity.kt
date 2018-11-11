package com.trackfinances.domasastrauskas.trackfinances

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.trackfinances.domasastrauskas.trackfinances.globals.AuthToken
import com.trackfinances.domasastrauskas.trackfinances.globals.GlobalUsers

class DashboardActivity : AppCompatActivity() {

    val globalAuthToken = AuthToken.Token
    val globalUsers = GlobalUsers.Users

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        Toast.makeText(applicationContext, "Successfully logged in.", Toast.LENGTH_LONG).show()


        if (globalUsers.users != null) {
            println("CURRENT USER: ${globalUsers.getUser()}")


        } else {
            println("User is null.")
        }

    }

    private fun retrieveAllExpenses(userId: Long) {

    }
}
