package com.example.meet.view.profile

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.meet.R
import com.example.meet.view.login.LoginActivity
import com.example.meet.viewmodel.ProfileViewModel
import com.example.meet.viewmodel.ProfileViewModelFactory
import com.google.firebase.auth.FirebaseUser


/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
    private lateinit var profileViewModel : ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nameTextView = view.findViewById<TextView>(R.id.profileNameTextView)
        val emailTextView = view.findViewById<TextView>(R.id.emailTextView)
        val careerTextView = view.findViewById<TextView>(R.id.profileCareerTextView)
        val locationTextView = view.findViewById<TextView>(R.id.profileLocationTextView)
        val changeEmailButton = view.findViewById<Button>(R.id.profilechangeEmailButton)
        val resetEmailEditText = view.findViewById<EditText>(R.id.resetEmailEditText)
        val resetEmailButton = view.findViewById<Button>(R.id.profileResetEmailButton)
        val profileDisplayEmailLayout = view.findViewById<LinearLayout>(R.id.profileDisplayEmailLayout)
        val profileResetEmailLayout = view.findViewById<LinearLayout>(R.id.profileResetEmailLayout)
        val profileDisplayPasswordLayout = view.findViewById<LinearLayout>(R.id.profileDisplayPasswordLayout)
        val profileResetPasswordLayout = view.findViewById<LinearLayout>(R.id.profileResetPasswordLayout)
        val profileChangePasswordButton = view.findViewById<Button>(R.id.profileChangePasswordButton)
        val profileResetPasswordEditText = view.findViewById<EditText>(R.id.profileResetPasswordEditText)
        val profileConfirmResetPasswordButton = view.findViewById<Button>(R.id.profileConfirmResetPasswordButton)
        val profileViewModelFactory = ProfileViewModelFactory(requireActivity().application)

        profileViewModel = ViewModelProvider(this, profileViewModelFactory).get(ProfileViewModel::class.java)
        profileViewModel.getUserMutableLiveData().observe(viewLifecycleOwner, object : Observer<FirebaseUser> {
            override fun onChanged(firebaseUser: FirebaseUser) {
                if(firebaseUser != null){
                    emailTextView.setText(firebaseUser.email)

                }
            }
        })

        profileViewModel.getIsLoggedOutLiveData().observe(viewLifecycleOwner, object : Observer<Boolean> {
            override fun onChanged(isLoggedOut: Boolean) {
                if(isLoggedOut){
                    val intent = Intent(context, LoginActivity::class.java)
                    startActivity(intent)
                }
            }
        })

        profileViewModel.getUserDataMutableLiveData().observe(viewLifecycleOwner, object :
            Observer<HashMap<String, Any>> {
            @SuppressLint("SetTextI18n")
            override fun onChanged(t: HashMap<String, Any>?) {
                if(profileViewModel.getUserDataMutableLiveData().value != null){
                    val firstName = t!!["firstName"]
                    val lastName = t["lastName"]
                    val location = t["city"]
                    val career = t["career"]
                    val company = t["company"]

                    nameTextView.setText("$firstName $lastName")
                    careerTextView.setText("$career, $company")
                    locationTextView.setText("$location")
                }
            }
        })

        changeEmailButton.setOnClickListener {
            toggleVisibility(profileDisplayEmailLayout)
            toggleVisibility(profileResetEmailLayout)
        }

        resetEmailButton.setOnClickListener {
            val newEmail = resetEmailEditText.text.toString()
            if(newEmail.length > 7) profileViewModel.changeEmail(newEmail)
            else Toast.makeText(context, "Could not change email", Toast.LENGTH_SHORT).show()
            toggleVisibility(profileDisplayEmailLayout)
            toggleVisibility(profileResetEmailLayout)
        }

        profileChangePasswordButton.setOnClickListener {
            toggleVisibility(profileDisplayPasswordLayout)
            toggleVisibility(profileResetPasswordLayout)
        }

        profileConfirmResetPasswordButton.setOnClickListener {
            val newPassword = profileResetPasswordEditText.text.toString()
            if(newPassword.length > 6) profileViewModel.changePassword(newPassword)
            else Toast.makeText(context, "password length must be greater than 6", Toast.LENGTH_SHORT).show()
            toggleVisibility(profileDisplayPasswordLayout)
            toggleVisibility(profileResetPasswordLayout)
        }

        val signOutButton = view.findViewById<Button>(R.id.profileSignOutButton)
        signOutButton.setOnClickListener {
            profileViewModel.signOut()
        }

    }

    fun toggleVisibility(view : View){
        view.visibility = if(view.visibility == View.VISIBLE){
            View.GONE
        }
        else{
            View.VISIBLE
        }
    }
}