package com.trackfinances.domasastrauskas.trackfinances

import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.trackfinances.domasastrauskas.trackfinances.adapters.ExpensesAdapter
import com.trackfinances.domasastrauskas.trackfinances.globals.AuthToken
import com.trackfinances.domasastrauskas.trackfinances.globals.GlobalUsers
import com.trackfinances.domasastrauskas.trackfinances.model.Expenses
import kotlinx.android.synthetic.main.activity_dashboard.*
import org.json.JSONArray
import java.lang.reflect.Type

class DashboardActivity : AppCompatActivity() {

    private val globalAuthToken = AuthToken.Token
    private val globalUsers = GlobalUsers.Users

    private var expenses = ArrayList<Expenses>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        Toast.makeText(applicationContext, "Successfully logged in.", Toast.LENGTH_LONG).show()


        if (globalUsers.users != null) {
            retrieveAllExpenses()
        } else {
            println("User is null.")
        }

    }

    private fun retrieveAllExpenses() {
        val allExpensesUrl = getString(R.string.backendUrl) + "/expenses/"
        val volleyQueue: RequestQueue? = Volley.newRequestQueue(applicationContext)

        val allExpensesRequest = object : JsonArrayRequest(Request.Method.GET, allExpensesUrl, null,
            Response.Listener<JSONArray> { response ->
                val type: Type = object : TypeToken<ArrayList<Expenses>>() {}.type
                expenses = Gson().fromJson(response.toString(), type)
                initExpensesRecyclerView(expenses)
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

    public fun addExpense(view: View) {
        val builder = AlertDialog.Builder(this)

        val viewInflated =
            LayoutInflater.from(this).inflate(R.layout.add_expense, findViewById(android.R.id.content), false)

        val title = viewInflated.findViewById(R.id.addExpenseTitle) as EditText
        val price = viewInflated.findViewById(R.id.addExpensePrice) as EditText
        val description = viewInflated.findViewById(R.id.addExpenseDescription) as EditText

        builder.setView(viewInflated)
            .setPositiveButton(R.string.addExpenseDialogAdd, DialogInterface.OnClickListener { dialog, which ->
                println("Expense title: ${title.text}")
                println("Expense price: ${price.text}")
                println("Expense description: ${description.text}")
            })
            .setNegativeButton(R.string.addExpenseDialogCancel, DialogInterface.OnClickListener { dialog, which ->
                //
            }).create().show()
    }

    private fun initExpensesRecyclerView(expenses: ArrayList<Expenses>) {
        Toast.makeText(applicationContext, "Should now display all expenses", Toast.LENGTH_LONG).show()
        val expensesAdapter = ExpensesAdapter(expenses)
        expensesRecyclerView.adapter = expensesAdapter
        expensesRecyclerView.layoutManager = LinearLayoutManager(this)
    }
}
