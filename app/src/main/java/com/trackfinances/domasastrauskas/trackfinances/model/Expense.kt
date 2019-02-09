package com.trackfinances.domasastrauskas.trackfinances.model

import java.math.BigDecimal

data class Expense(
    val userId: Long,
    val title: String,
    val price: BigDecimal,
    val description: String
) {
}