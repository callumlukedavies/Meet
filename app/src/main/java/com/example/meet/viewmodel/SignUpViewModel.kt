package com.example.meet.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.core.util.PatternsCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.meet.model.MainRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

//@RequiresApi(Build.VERSION_CODES.P)
class SignUpViewModel(application: Application) : AndroidViewModel(application) {
    private var firstName : String = ""
    private var lastName : String = ""
    private var email : String = ""
    private var city : String = ""
    private var country : String = ""
    private var jobTitle : String = ""
    private var company : String = ""
    private var password : String = ""
    private var confirmPass : String = ""
    private var mainRepository : MainRepository
    private var firebaseAuth : FirebaseAuth
    private var userMutableLiveData : MutableLiveData<FirebaseUser>

    init {
        mainRepository = MainRepository()
        firebaseAuth = FirebaseAuth.getInstance()
        userMutableLiveData = MutableLiveData()
    }

    fun setFirstName(newFirstName : String) {
        this.firstName = newFirstName
    }

    fun getFirstName() : String {
        return this.firstName
    }

    fun setLastName(newLastName : String) {
        this.lastName = newLastName
    }

    fun getLastName() : String {
        return this.lastName
    }

    fun setEmail(newEmail : String) {
        this.email = newEmail
    }

    fun getEmail() : String {
        return this.email
    }

    fun setCity(newCity : String) {
        city = newCity
    }

    fun setCountry(newCountry : String) {
        country = newCountry
    }

    fun setJobTitle(newJobTitle : String) {
        jobTitle = newJobTitle
    }

    fun setCompany(newCompany : String) {
        company = newCompany
    }

    fun setPassword(newPassword : String) {
        this.password = newPassword
    }

    fun getPassword() : String {
        return this.password
    }

    fun setConfirmPass(newConfirmPassword : String) {
        this.confirmPass = newConfirmPassword
    }

    fun getConfirmPass() : String {
        return this.confirmPass
    }

    fun getUserMutableLiveData() : MutableLiveData<FirebaseUser> {
        return this.userMutableLiveData
    }

    fun signUp(){
        //Signs the user up with email and password credentials to firebase.
        //If successful, create a new entry for the user in the database storing the
        //users data.
        mainRepository.signUp(email, password).addOnCompleteListener{
            if(it.isSuccessful){
                userMutableLiveData.postValue(firebaseAuth.currentUser)

                val user = firebaseAuth.currentUser
                val userId: String = user!!.uid
                val hashMap: HashMap<String, Any> = HashMap()
                hashMap.put("userId", userId)
                hashMap.put("firstName", firstName)
                hashMap.put("lastName", lastName)
                hashMap.put("career", jobTitle)
                hashMap.put("company", company)
                hashMap.put("city", city)
                hashMap.put("country", country)
                hashMap.put("friends", mutableListOf<String>())
                hashMap.put("events", mutableListOf<String>())

                mainRepository.sendUserDataToDatabase(hashMap).addOnCompleteListener{
                    if(it.isSuccessful){
                        Toast.makeText(getApplication(), "User data added to database", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(getApplication(), "User data could not be added to database", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else{
                Toast.makeText(getApplication(), "Unable to Log In", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {

        fun validateName(name: String): Boolean {
            //Validates the name EditText values
            if(name.isEmpty()) return false
            else if(containsNumbers(name)) return false
            else if(containsSpecialChar(name)) return false
            else return !containsWhiteSpace(name)
        }

        fun validateEmail(email: String): Boolean {
            //Validates the Email EditText value
            return email.isNotEmpty() && PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()
        }

        fun validateJobTitle(job: String): Boolean {
            //Validates the Job Title EditText value
            if(job.isEmpty()) return false
            else if(containsNumbers(job)) return false
            else return containsNoSpecialChar(job)
        }

        fun validateCompany(company: String): Boolean {
            //Validates the company EditText value
            if(company.isEmpty()) return false
            else if(containsNumbers(company)) return false
            else return containsNoSpecialChar(company)
        }

        fun validateCity(city: String): Boolean {
            //Validates the city EditText value
            if(city.isEmpty()) return false
            else if(containsNumbers(city)) return false
            else return containsNoSpecialChar(city)
        }

        fun validateCountry(country: String): Boolean {
            //Validates the country EditText value
            if(country.isEmpty()) return false
            else if(containsNumbers(country)) return false
            else return containsNoSpecialChar(country)
        }

        fun validatePassword(password: String) : Boolean {
            //Checks if a password is non empty, greater than 8 characters,
            //does not contain whitespace, contains numbers, contains an upper
            //case letter and contains a special character. Returns false if
            //password does not meet these conditions.
            if(password.isEmpty()){
                println("password empty")
                return false
            }
            else if(password.length < 8){
                println("Password too short")
                return false
            }
            else if(containsWhiteSpace(password)) {
                println("Password contains space")
                return false
            }
            else if(!containsNumbers(password)){
                println("Password doesn't contain number")

                return false
            }
            else if(!containsUpperCase(password)){
                println("Password doesn't contain upper case letter")
                return false
            }
            else return containsSpecialChar(password)
        }

        fun matchPassword(password: String, confirmPassword: String): Boolean {
            return (password == confirmPassword)
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

        private fun containsNoSpecialChar(input: String): Boolean {
            //Returns true if input contains no special characters, else false
            for(char: Char in input){
                if(!char.isLetterOrDigit() && !char.isWhitespace()) return false
            }
            return true
        }
    }
}