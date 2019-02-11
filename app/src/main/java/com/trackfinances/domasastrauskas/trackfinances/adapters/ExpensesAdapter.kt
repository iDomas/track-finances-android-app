package com.trackfinances.domasastrauskas.trackfinances.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.trackfinances.domasastrauskas.trackfinances.ClickListener
import com.trackfinances.domasastrauskas.trackfinances.R
import com.trackfinances.domasastrauskas.trackfinances.model.Expenses
import kotlinx.android.synthetic.main.expense.view.*
import java.lang.ref.WeakReference

class ExpensesAdapter(val expenses: ArrayList<Expenses>, val listener: ClickListener) :
    RecyclerView.Adapter<ExpensesAdapter.ViewHolder>() {

    private val mListener: ClickListener = listener;
    private val mExpenses: ArrayList<Expenses> = expenses;

    inner class ViewHolder(val itemVIew: View, val listener: ClickListener) : RecyclerView.ViewHolder(itemVIew),
        View.OnClickListener {
        val expenseTitle: TextView
        val expensePrice: TextView
        val expenseDescription: TextView
        val editButton: Button
        val deleteButton: Button
        private val listenerRef: WeakReference<ClickListener>;

        init {
            listenerRef = WeakReference<ClickListener>(listener)
            expenseTitle = itemVIew.findViewById(R.id.expenseTitle)
            expensePrice = itemVIew.findViewById(R.id.expensePrice)
            expenseDescription = itemVIew.findViewById(R.id.expenseDescription)
            editButton = itemVIew.buttonEditExpense
            deleteButton = itemVIew.buttonDeleteExpense

            expenseTitle.setOnClickListener(this)
            expensePrice.setOnClickListener(this)
            expenseDescription.setOnClickListener(this)
            editButton.setOnClickListener(this)
            deleteButton.setOnClickListener(this)
        }

        override fun onClick(v: View) {
//            Toast.makeText(v!!.context, "Item pressed", Toast.LENGTH_SHORT).show()
            listenerRef.get()!!.onPositionClicked(adapterPosition, v)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val expenseView = inflater.inflate(R.layout.expense, parent, false)
        return ViewHolder(expenseView, mListener)
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