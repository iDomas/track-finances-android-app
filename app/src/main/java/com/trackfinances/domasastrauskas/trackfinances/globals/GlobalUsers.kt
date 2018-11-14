package com.trackfinances.domasastrauskas.trackfinances.globals

class GlobalUsers {

    companion object Users {
        var allUsers: ArrayList<Users>? = null
        fun getUsers(): ArrayList<Users> {
            return allUsers!!
        }

        var users: String? = null
        fun getUser(): String {
            return users!!
        }
    }

}