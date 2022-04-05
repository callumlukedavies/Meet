package com.example.meet.viewmodel

import android.app.Application
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.meet.model.MainRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot

@RequiresApi(Build.VERSION_CODES.P)
class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    private var mainRepository : MainRepository
    private var firebaseAuth : FirebaseAuth
    private var userMutableLiveData : MutableLiveData<FirebaseUser>
    private var isLoggedOutLiveData : MutableLiveData<Boolean>
    private var userDataMutableLiveData : MutableLiveData<HashMap<String, Any>>
    private var userHashMap : HashMap<String, Any>

    init {
        mainRepository = MainRepository()
        firebaseAuth = FirebaseAuth.getInstance()
        userMutableLiveData = MutableLiveData()
        isLoggedOutLiveData = MutableLiveData()
        userDataMutableLiveData = MutableLiveData()
        userHashMap = HashMap()

        if (firebaseAuth.currentUser != null){
            isLoggedOutLiveData.postValue(false)
            userMutableLiveData.postValue(firebaseAuth.currentUser)
            getUserDataFromMap()
        }
    }

    fun signOut(){
        //Signs out the user and unsubscribes the user from notifications for that account
        mainRepository.unsubscribeFromNotifications()
        firebaseAuth.signOut()
        isLoggedOutLiveData.postValue(true)
    }

    private fun getUserDataFromMap(){
        // Retrieve users data from the database, to populate the views in the profile
        mainRepository.getMyUserData().addOnCompleteListener {
            if (it.isSuccessful) {
                Log.d("firebase Communications", "Get user data from database: Successful")
                for (snap: DataSnapshot in it.result!!.children) {
                    userHashMap.put(snap.key as String, snap.value as Any)
                    userDataMutableLiveData.postValue(userHashMap)
                }
            } else {
                Log.d("firebase Communications", "Get user data from database: Unsuccessful")
            }
        }
    }

    fun changeEmail(newEmail : String) {
        // If new email address is valid, change the users email address
        if(validateEmailAddress(newEmail)){
            mainRepository.changeEmail(newEmail).addOnCompleteListener {
                if (it.isSuccessful){
                    Log.d("Firebase comms", "Changed user email to $newEmail")
                    userMutableLiveData.postValue(firebaseAuth.currentUser)
                }
                else {
                    Log.d("Firebase comms", "Could not update user email: ${it.exception.toString()}")
                    Toast.makeText(getApplication(), it.exception.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(getApplication(), "Please enter a valid email address", Toast.LENGTH_SHORT).show()
        }
    }

    fun changePassword(newPassword : String){
        //Change password to newPassword if it passes validation
        if(validatePassword(newPassword)){
            mainRepository.changePassword(newPassword).addOnCompleteListener{
                if (it.isSuccessful){
                    Log.d("Firebase comms", "Changed user email to $newPassword")
                    userMutableLiveData.postValue(firebaseAuth.currentUser)
                }
                else
                {
                    Log.d("Firebase comms", "Could not update user password: ${it.exception.toString()}")
                    Toast.makeText(getApplication(), it.exception.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(getApplication(), "Given password does not meet requirements", Toast.LENGTH_SHORT).show()
        }
    }

    fun getIsLoggedOutLiveData() : MutableLiveData<Boolean> {
        //Returns the MutableLiveData object for the users logged-in status
        return this.isLoggedOutLiveData
    }

    fun getUserMutableLiveData() : MutableLiveData<FirebaseUser> {
        //Returns the MutableLiveData object for the currently signed in firebase user
        return this.userMutableLiveData
    }

    fun getUserDataMutableLiveData() : MutableLiveData<HashMap<String, Any>> {
        //Returns the MutableLiveData object containing the currently signed in
        // users personal data
        return this.userDataMutableLiveData
    }

    companion object {
        fun validateEmailAddress(emailAddress: String): Boolean {
            return LoginViewModel.validateEmailAddress(emailAddress)
        }

        fun validatePassword(password: String): Boolean {
            return LoginViewModel.validatePassword(password)
        }
    }
}