package com.example.meet.view.login

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.meet.R
import com.example.meet.view.home.HomeActivity
import com.example.meet.view.signup.SignUpActivity
import com.example.meet.viewmodel.LoginViewModel
import com.example.meet.viewmodel.LoginViewModelFactory
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity() {

    private val SIGN_UP_REQUEST_CODE = 1
    private lateinit var loginButton: Button
    private lateinit var signUpButton: Button
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginViewModel: LoginViewModel

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val loginViewModelFactory = LoginViewModelFactory(application)
        loginButton = findViewById(R.id.loginButton)
        signUpButton = findViewById(R.id.signUpButton)
        emailEditText = findViewById(R.id.editTextEmailAddress)
        passwordEditText = findViewById(R.id.editTextPassword)

        loginViewModel = ViewModelProvider(this, loginViewModelFactory).get(LoginViewModel::class.java)
        loginViewModel.getUserMutableLiveData().observe(this, object : Observer<FirebaseUser>{
            override fun onChanged(firebaseUser: FirebaseUser) {
                if(firebaseUser != null){

                    val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                }
            }
        })

        loginButton.setOnClickListener{
            loginUser(emailEditText, passwordEditText)
        }

        signUpButton.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivityForResult(intent, SIGN_UP_REQUEST_CODE)
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    fun loginUser(emailEditText: EditText, passwordEditText : EditText){
        loginViewModel.setEmail(emailEditText.text.toString())
        loginViewModel.setPassword(passwordEditText.text.toString())
        var isValidLogin = true

        if(TextUtils.isEmpty(loginViewModel.getEmail())){
            Toast.makeText(applicationContext, "Please enter an email to login", Toast.LENGTH_SHORT).show()
            isValidLogin = false
        }

//        if(loginViewModel.getEmail().contains('{') )

        if(TextUtils.isDigitsOnly(loginViewModel.getEmail())){
            Toast.makeText(applicationContext, "Please enter a valid email address to login", Toast.LENGTH_SHORT).show()
            isValidLogin = false
        }

        if(TextUtils.isEmpty(loginViewModel.getPassword())){
            Toast.makeText(applicationContext, "Please enter a password to login", Toast.LENGTH_SHORT).show()
            isValidLogin = false
        }

        if(isValidLogin) loginViewModel.login()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString("emailText", emailEditText.text.toString())
        outState.putString("passwordText", passwordEditText.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        emailEditText.setText(savedInstanceState.getString(("emailText")))
        passwordEditText.setText(savedInstanceState.getString(("passwordText")))
    }
}
