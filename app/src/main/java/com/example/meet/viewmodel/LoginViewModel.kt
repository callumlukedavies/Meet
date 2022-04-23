package com.example.meet.viewmodel

import android.app.Application
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.util.PatternsCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.meet.model.MainRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


@RequiresApi(Build.VERSION_CODES.P)
class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private var email = ""
    private var password = ""
    private var mainRepository : MainRepository
    private var userMutableLiveData : MutableLiveData<FirebaseUser>
    private var firebaseAuth : FirebaseAuth

    init {
        mainRepository = MainRepository()
        userMutableLiveData = MutableLiveData()
        firebaseAuth = FirebaseAuth.getInstance()
        if(firebaseAuth.currentUser != null){
            userMutableLiveData.postValue(firebaseAuth.currentUser)
        }
    }

    fun login() {
        //Logs the user in to their account, and subscribes the user to notifications
        //associated with their account
        mainRepository.login(email, password).addOnCompleteListener{
            if(it.isSuccessful){
                userMutableLiveData.postValue(firebaseAuth.currentUser)
                mainRepository.subscribeToNotifications()
            }
            else{
                Toast.makeText(getApplication(), "Unable to Log In", Toast.LENGTH_SHORT).show()
                Log.d("failure", "Unable to log in")
            }
        }
    }

    fun getEmail() : String {
        return this.email
    }

    fun setEmail(newEmail : String){
        this.email = newEmail
    }

    fun getPassword() : String {
        return this.password
    }

    fun setPassword(newPassword : String){
        this.password = newPassword
    }

    fun getUserMutableLiveData() : MutableLiveData<FirebaseUser> {
        return userMutableLiveData
    }

    companion object {

        fun validateEmailAddress(email: String) : Boolean {
            //Returns whether the email address matches a valid
            //email address pattern.
            return PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()
        }

        fun validatePassword(password: String) : Boolean {
            //Checks if a password is non empty, greater than 8 characters,
            //does not contain whitespace, contains numbers, contains an upper
            //case letter and contains a special character. Returns false if
            //password does not meet these conditions.
            if(password.isEmpty()){
                return false
            }
            else if(password.length < 8){
                return false
            }
            else if(containsWhiteSpace(password)) {
                return false
            }
            else if(!containsNumbers(password)){
                return false
            }
            else if(!containsUpperCase(password)){
                return false
            }
            else return containsSpecialChar(password)
        }

        private fun containsNumbers(input: String): Boolean {
            //Returns true if input contains a numerical character, else false
            for(char: Char in input){
                if(char.isDigit()) return true
            }
            return false
        }

        private fun containsUpperCase(input: String): Boolean {
            //Returns true if input contains an uppercase character, else false
            for(char: Char in input){
                if(char.isUpperCase()) return true
            }
            return false
        }

        private fun containsWhiteSpace(input: String): Boolean {
            //Returns true if input contains whitespace, else false
            for(char: Char in input){
                if(char.isWhitespace()) return true
            }
            return false
        }

        private fun containsSpecialChar(input: String): Boolean {
            //Returns true if input contains a special character, else false
            for(char: Char in input){
                if(!char.isLetterOrDigit()) return true
            }
            return false
        }

    }
}