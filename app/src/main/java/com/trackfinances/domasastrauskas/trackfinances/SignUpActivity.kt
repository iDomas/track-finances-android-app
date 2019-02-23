package com.trackfinances.domasastrauskas.trackfinances

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.trackfinances.domasastrauskas.trackfinances.validators.TextValidator
import kotlinx.android.synthetic.main.activity_sign_up.*
import java.util.regex.Pattern

class SignUpActivity : AppCompatActivity() {

    val validationChecks: ArrayList<Boolean> = arrayListOf(false, false, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        Toast.makeText(applicationContext, "Currently on development", Toast.LENGTH_LONG).show()

        enterUsername.addTextChangedListener(object : TextValidator(enterUsername) {
            override fun validate(textView: TextView, text: String) {
                Toast.makeText(applicationContext, "You entered: " + text, Toast.LENGTH_SHORT).show();
                val patternMatcher: Pattern = Pattern.compile("[A-Za-z0-9_]+")
                if (!patternMatcher.matcher(text).matches()) {
                    usernameWarning.text = "Username do not match pattern [A-Za-z0-9]"
                    validationChecks[0] = false
                } else if (!isUsernameUsed(text)) {
                    usernameWarning.text = "Username already taken."
                    validationChecks[0] = false
                } else {
                    usernameWarning.text = ""
                    validationChecks[0] = true
                }
            }
        })
        enterPassword.addTextChangedListener(object : TextValidator(enterPassword) {
            override fun validate(textView: TextView, text: String) {
                Toast.makeText(applicationContext, "You entered password: " + text, Toast.LENGTH_SHORT).show();
            }
        })
        retypePassword.addTextChangedListener(object : TextValidator(retypePassword) {
            override fun validate(textView: TextView, text: String) {
                Toast.makeText(applicationContext, "You retyped passowrd: " + text, Toast.LENGTH_SHORT).show();
            }
        })

    }



    fun signUp(view: View) {
        // TODO validate password

        //  TODO check if username exists

        // TODO insert user into database

        if (validationChecks[0] && validationChecks[1] && validationChecks[2]) {
            Toast.makeText(applicationContext, "Can register user", Toast.LENGTH_SHORT).show()
        }
    }

    fun goBack(view: View) {
        val loginActivity = Intent(applicationContext, LoginActivity::class.java)
        startActivity(loginActivity)
        finish()
    }

    private fun isUsernameUsed(username: String): Boolean {
        // TODO send request to backend and check if user exists with given username

        return false;
    }
}
