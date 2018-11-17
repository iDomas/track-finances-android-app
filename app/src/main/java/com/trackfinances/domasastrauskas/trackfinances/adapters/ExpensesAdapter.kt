package com.trackfinances.domasastrauskas.trackfinances.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.trackfinances.domasastrauskas.trackfinances.R
import com.trackfinances.domasastrauskas.trackfinances.model.Expenses

class ExpensesAdapter(val expenses: ArrayList<Expenses>) : RecyclerView.Adapter<ExpensesAdapter.ViewHolder>() {

    private val mExpenses: ArrayList<Expenses> = expenses;

    inner class ViewHolder(val itemVIew: View) : RecyclerView.ViewHolder(itemVIew) {

        val expenseTitle: TextView
        val expensePrice: TextView
        val expenseDescription: TextView

        init {
            expenseTitle = itemVIew.findViewById(R.id.expenseTitle)
            expensePrice = itemVIew.findViewById(R.id.expensePrice)
            expenseDescription = itemVIew.findViewById(R.id.expenseDescription)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val expenseView = inflater.inflate(R.layout.expense, parent, false)
        return ViewHolder(expenseView)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val expense = mExpenses[position]

        val expenseTitle = viewHolder.expenseTitle
        expenseTitle.text = expense.title
        val expensePrice = viewHolder.expensePrice
        expensePrice.text = expense.price.toString()
        val expenseDescription = viewHolder.expenseDescription
        expenseDescription.text = expense.description
    }

    override fun getItemCount(): Int {
        return mExpenses.size
    }

}