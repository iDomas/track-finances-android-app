package com.trackfinances.domasastrauskas.trackfinances.model

import java.math.BigDecimal

data class Expenses (
    val id: Long,
    val userId: Long,
    val price: BigDecimal,
    val title: String,
    val description: String
) {}