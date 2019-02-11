package com.trackfinances.domasastrauskas.trackfinances

import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.trackfinances.domasastrauskas.trackfinances.adapters.ExpensesAdapter
import com.trackfinances.domasastrauskas.trackfinances.globals.AuthToken
import com.trackfinances.domasastrauskas.trackfinances.globals.GlobalUsers
import com.trackfinances.domasastrauskas.trackfinances.model.Expense
import com.trackfinances.domasastrauskas.trackfinances.model.Expenses
import com.trackfinances.domasastrauskas.trackfinances.model.Users
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.edit_expense.view.*
import kotlinx.android.synthetic.main.expense.view.*
import kotlinx.android.synthetic.main.expense_details.view.*
import org.json.JSONArray
import org.json.JSONObject
import java.lang.reflect.Type
import java.math.BigDecimal

class DashboardActivity : AppCompatActivity() {

    private val TAG: String = "DASHBOARD ACTIVITY";

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

    private fun initExpensesRecyclerView(expenses: ArrayList<Expenses>) {
        val expensesAdapter = ExpensesAdapter(expenses, object : ClickListener {
            override fun onPositionClicked(position: Int, v: View) {
                // TODO here hose editing pop up
                if (v is Button) {
                    if (v.buttonEditExpense != null) {
                        editExpense(position)
                    } else if (v.buttonDeleteExpense != null) {
                        deleteExpense(position)
                    }
                }

                if (v is TextView && v !is Button) {
//                    Toast.makeText(applicationContext, "Here will be expense details", Toast.LENGTH_SHORT).show()
                    viewExpenseDetails(expenses[position])
                }
            }

        })
        expensesRecyclerView.adapter = expensesAdapter
        expensesRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    fun addExpense(view: View) {
        val builder = AlertDialog.Builder(this)

        val viewInflated =
            LayoutInflater.from(this).inflate(R.layout.add_expense, findViewById(android.R.id.content), false)

        val title = viewInflated.findViewById(R.id.addExpenseTitle) as EditText
        val price = viewInflated.findViewById(R.id.addExpensePrice) as EditText
        val description = viewInflated.findViewById(R.id.addExpenseDescription) as EditText

        builder.setView(viewInflated)
            .setPositiveButton(R.string.addExpenseDialogAdd, DialogInterface.OnClickListener { dialog, which ->
                sendExpenseToDB(title.text.toString(), BigDecimal(price.text.toString()), description.text.toString())
            })
            .setNegativeButton(R.string.addExpenseDialogCancel, DialogInterface.OnClickListener { dialog, which ->
                //
            }).create().show()
    }

    private fun editExpense(position: Int) {
        val builder = AlertDialog.Builder(this)
        val viewInflated =
            LayoutInflater.from(this).inflate(R.layout.edit_expense, findViewById(R.id.content), false)

        val title = viewInflated.editExpenseTitle as EditText
        val price = viewInflated.editExpensePrice as EditText
        val description = viewInflated.editExpenseDescription as EditText

        title.setText(expenses[position].title)
        price.setText(String.format("%.2f", expenses[position].price))
        description.setText(expenses[position].description)

        builder.setView(viewInflated)
            .setPositiveButton(getString(R.string.editExpenseEdit), DialogInterface.OnClickListener { dialog, which ->
                updateExpenseInDB(
                    position,
                    expenses[position].id,
                    title.text.toString(),
                    BigDecimal(price.text.toString()),
                    description.text.toString()
                )
            })
            .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which ->
                Toast.makeText(applicationContext, "Edit canceled", Toast.LENGTH_SHORT).show()
            }).create().show()
    }

    private fun deleteExpense(position: Int) {
        val builder = AlertDialog.Builder(this)
        val viewInflated =
            LayoutInflater.from(this).inflate(R.layout.delete_expense, findViewById(R.id.content), false)

        builder.setView(viewInflated)
            .setPositiveButton("Delete", DialogInterface.OnClickListener { dialog, which ->
                deleteExpenseInDB(expenses[position].id, position)
            })
            .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which ->
                Toast.makeText(applicationContext, "Delete canceled", Toast.LENGTH_SHORT).show()
            }).create().show()
    }

    private fun viewExpenseDetails(expense: Expenses) {
        val builder = AlertDialog.Builder(this)
        val viewInflated =
            LayoutInflater.from(this).inflate(R.layout.expense_details, findViewById(R.id.content), false)

        val title = viewInflated.detailsExpenseTitle as TextView
        val price = viewInflated.detailsExpensePrice as TextView
        val description = viewInflated.detailsExpenseDescription as TextView

        title.setText(expense.title)
        price.setText(String.format("%.2f", expense.price))
        description.setText(expense.description)

        builder.setView(viewInflated)
            .setNegativeButton("Close", DialogInterface.OnClickListener { dialog, which ->
                Toast.makeText(applicationContext, "Details view closed.", Toast.LENGTH_SHORT).show()
            }).create().show()
    }

    private fun sendExpenseToDB(title: String, price: BigDecimal, description: String) {
        val userId = Gson().fromJson(globalUsers.getUser(), Users::class.java).id
        val expense: Expense = Expense(userId, title, price, description);

        val expenseUrl = getString(R.string.backendUrl) + "/expenses/"
        val volleyQueue: RequestQueue? = Volley.newRequestQueue(applicationContext)

        val expenseRequest =
            object : JsonObjectRequest(Request.Method.POST, expenseUrl, null,
                Response.Listener<JSONObject> { response ->
                    val expense = Gson().fromJson(response.toString(), Expenses::class.java)
                    expenses.add(expense)
                    println("Inserted expense: $expense")
                    initExpensesRecyclerView(expenses)
                },
                Response.ErrorListener { error ->
                    Log.e(TAG, "Error editing: $error")
                }
            ) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Authorization"] = globalAuthToken.getToken()

                    return headers
                }

                override fun getBody(): ByteArray {
                    val expenseToJson = Gson().toJson(expense)
                    return expenseToJson.toByteArray()
                }
            }

        volleyQueue?.add(expenseRequest)
    }

    private fun updateExpenseInDB(
        expensePosition: Int,
        expenseId: Long,
        title: String,
        price: BigDecimal,
        description: String
    ) {
        val userId = Gson().fromJson(globalUsers.getUser(), Users::class.java).id
        val expense: Expense = Expense(userId, title, price, description)

        val expenseUpdateUrl = getString(R.string.backendUrl) + "/expenses/" + expenseId
        val volleyQueue: RequestQueue? = Volley.newRequestQueue(applicationContext)

        val expenseUpdateRequest =
            object : JsonObjectRequest(Request.Method.PUT, expenseUpdateUrl, null,
                Response.Listener<JSONObject> { response ->
                    Log.i(TAG, "Successfully updated")
                    val expense = Gson().fromJson(response.toString(), Expenses::class.java)
                    expenses[expensePosition] = expense
                    initExpensesRecyclerView(expenses)
                },
                Response.ErrorListener { error ->
                    Log.e(TAG, "Error updating: $error")
                }
            ) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Authorization"] = globalAuthToken.getToken()

                    return headers
                }

                override fun getBody(): ByteArray {
                    val expenseToJson = Gson().toJson(expense)
                    return expenseToJson.toByteArray()
                }
            }

        volleyQueue?.add(expenseUpdateRequest);
    }

    private fun deleteExpenseInDB(expenseId: Long, expensePositionInArray: Int) {
        val expenseDeleteUrl = getString(R.string.backendUrl) + "/expenses/" + expenseId
        val volleyQueue: RequestQueue? = Volley.newRequestQueue(applicationContext)

        val expensesDeleteRequest =
            object : JsonObjectRequest(Request.Method.DELETE, expenseDeleteUrl, null,
                Response.Listener<JSONObject> { response ->
                    Log.i(TAG, "Successfully deleted expense.")
                    expenses.remove(expenses[expensePositionInArray]);
                    initExpensesRecyclerView(expenses)
                },
                Response.ErrorListener { error ->
                    Log.e(TAG, "Error deleting expense: $error")
                }
            ) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Authorization"] = globalAuthToken.getToken()

                    return headers
                }
            }

        volleyQueue?.add(expensesDeleteRequest)
    }
}
