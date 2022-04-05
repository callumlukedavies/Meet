package com.example.meet.view.signup

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.meet.R
import com.example.meet.viewmodel.SignUpViewModel
import com.example.meet.viewmodel.SignUpViewModelFactory
import com.google.firebase.auth.FirebaseUser

class SignUpActivity : AppCompatActivity(), SignUpOneFragment.OnEventListener,
    SignUpTwoFragment.OnEventListener, SignUpThreeFragment.OnEventListener {

    private lateinit var signUpViewModel : SignUpViewModel
    private var signUpOneFragment: SignUpOneFragment? = null
    private var signUpTwoFragment: SignUpTwoFragment? = null
    private var signUpThreeFragment: SignUpThreeFragment? = null
    private val FRAGMENT_ONE_TAG = "SignUpOneFragment"
    private val FRAGMENT_TWO_TAG = "SignUpTwoFragment"
    private val FRAGMENT_THREE_TAG = "SignUpThreeFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        Log.d("SignUpActivity", "onCreate()")
        if(savedInstanceState != null){
            if(supportFragmentManager.findFragmentByTag(FRAGMENT_ONE_TAG) != null)
                signUpOneFragment = supportFragmentManager.findFragmentByTag(FRAGMENT_ONE_TAG)
                    as SignUpOneFragment
            else signUpOneFragment = SignUpOneFragment()

            signUpTwoFragment = if(supportFragmentManager.findFragmentByTag(FRAGMENT_TWO_TAG) != null)
                supportFragmentManager.findFragmentByTag(FRAGMENT_TWO_TAG)
                        as SignUpTwoFragment
            else SignUpTwoFragment()

            signUpThreeFragment = if(supportFragmentManager.findFragmentByTag(FRAGMENT_THREE_TAG) != null)
                supportFragmentManager.findFragmentByTag(FRAGMENT_THREE_TAG)
                        as SignUpThreeFragment
            else SignUpThreeFragment()
        }else {
            signUpOneFragment = SignUpOneFragment()
            signUpTwoFragment = SignUpTwoFragment()
            signUpThreeFragment = SignUpThreeFragment()

            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.add(R.id.fragmentLinearLayout, signUpOneFragment!!, FRAGMENT_ONE_TAG)
            fragmentTransaction.addToBackStack(FRAGMENT_ONE_TAG)
            fragmentTransaction.commit()
        }

        val actionBar = supportActionBar
        actionBar?.title = "Sign Up"
        actionBar?.setDisplayHomeAsUpEnabled(true)

        val signUpViewModelFactory = SignUpViewModelFactory(application)

        signUpViewModel = ViewModelProvider(this, signUpViewModelFactory).get(SignUpViewModel::class.java)
        signUpViewModel.getUserMutableLiveData().observe(this, object : Observer<FirebaseUser>{
            override fun onChanged(firebaseUser: FirebaseUser) {
                if(firebaseUser != null){
                    finish()
                }
            }
        })
    }

    private fun changeFragment(backStackCount : Int) {

        when(backStackCount){
            1 -> {
                val fragmentTransaction = supportFragmentManager.beginTransaction()
                fragmentTransaction.hide(signUpOneFragment!!)
                fragmentTransaction.add(R.id.fragmentLinearLayout, signUpTwoFragment!!, FRAGMENT_TWO_TAG)
                fragmentTransaction.addToBackStack(FRAGMENT_TWO_TAG)
                fragmentTransaction.commit()
            }

            2 -> {
                val fragmentTransaction = supportFragmentManager.beginTransaction()
                fragmentTransaction.hide(signUpTwoFragment!!)
                fragmentTransaction.add(R.id.fragmentLinearLayout, signUpThreeFragment!!, FRAGMENT_THREE_TAG)
                fragmentTransaction.addToBackStack(FRAGMENT_THREE_TAG)
                fragmentTransaction.commit()
            }
        }
    }

    override fun fragmentOneCompleted(firstName: String, lastName: String, email: String) {
        signUpViewModel.setFirstName(firstName)
        signUpViewModel.setLastName(lastName)
        signUpViewModel.setEmail(email)

        changeFragment(supportFragmentManager.backStackEntryCount)
    }

    override fun fragmentTwoCompleted(
        city: String,
        country: String,
        jobTitle: String,
        company: String
    ) {
        signUpViewModel.setCity(city)
        signUpViewModel.setCountry(country)
        signUpViewModel.setJobTitle(jobTitle)
        signUpViewModel.setCompany(company)

        changeFragment(supportFragmentManager.backStackEntryCount)
    }

    override fun fragmentThreeCompleted(password: String, confirmPassword: String) {
        signUpViewModel.setPassword(password)
        signUpViewModel.setConfirmPass(confirmPassword)

        signUpViewModel.signUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //Set up the back button for the support action bar
        when(item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        println("OnBackPressed")
        println(supportFragmentManager.backStackEntryCount)
        if(supportFragmentManager.backStackEntryCount == 0){
            finish()
        }
    }
}