package com.trackfinances.domasastrauskas.trackfinances

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.trackfinances.domasastrauskas.trackfinances.globals.AuthToken
import com.trackfinances.domasastrauskas.trackfinances.globals.GlobalUsers
import com.trackfinances.domasastrauskas.trackfinances.model.Expenses
import com.trackfinances.domasastrauskas.trackfinances.model.Users
import org.json.JSONArray

class DashboardActivity : AppCompatActivity() {

    private val globalAuthToken = AuthToken.Token
    private val globalUsers = GlobalUsers.Users

    private val expenses = ArrayList<Expenses>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        Toast.makeText(applicationContext, "Successfully logged in.", Toast.LENGTH_LONG).show()


        if (globalUsers.users != null) {
            val userId = Gson().fromJson(globalUsers.getUser(), Users::class.java).id

            retrieveAllExpenses(userId)
        } else {
            println("User is null.")
        }

    }

    private fun retrieveAllExpenses(userId: Long) {
        val allExpensesUrl = getString(R.string.backendUrl) + "/expenses/"
        val volleyQueue: RequestQueue? = Volley.newRequestQueue(applicationContext)

        val allExpensesRequest = object : JsonArrayRequest(Request.Method.GET, allExpensesUrl, null,
            Response.Listener<JSONArray> { response ->
                println("Expenses by user id: $response")
            },
            Response.ErrorListener { error -> println("Expenses ERR: $error") }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = globalAuthToken.getToken()

                return headers
            }
        }

        volleyQueue!!.add(allExpensesRequest);
    }

    private fun addExpense(userId: Long) {

    }
}
