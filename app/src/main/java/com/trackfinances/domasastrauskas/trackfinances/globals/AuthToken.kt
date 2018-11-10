package com.trackfinances.domasastrauskas.trackfinances.globals

class AuthToken {

    companion object Token {
        var authToken: String? = null
        fun getToken(): String {
            return authToken!!
        }
    }
}