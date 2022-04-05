package com.example.meet.view.signup

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.meet.R
import com.example.meet.viewmodel.SignUpViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [SignUpOneFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SignUpOneFragment : Fragment() {

    private lateinit var onEventListener: OnEventListener
    private lateinit var signUpFirstNameEditText: EditText
    private lateinit var signUpFirstNameInvalidTextView: TextView
    private lateinit var signUpLastNameEditText: EditText
    private lateinit var signUpLastNameInvalidTextView: TextView
    private lateinit var signUpEmailEditText: EditText
    private lateinit var signUpEmailInvalidTextView: TextView
    private var signUpFirstNameValidity = 0
    private var signUpLastNameValidity = 0
    private var signUpEmailValidity = 0

    interface OnEventListener {
        fun fragmentOneCompleted(firstName: String, lastName: String, email: String)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up_one, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val signUpFragmentOneButton = view.findViewById<Button>(R.id.signUpFragmentOneButton)
        signUpFirstNameEditText = view.findViewById(R.id.signUpFirstNameEditText)
        signUpLastNameEditText = view.findViewById(R.id.signUpLastNameEditText)
        signUpEmailEditText = view.findViewById(R.id.signUpEmailEditText)
        signUpFirstNameInvalidTextView = view.findViewById(R.id.signUpFirstNameInvalidTextView)
        signUpLastNameInvalidTextView = view.findViewById(R.id.signUpLastNameInvalidTextView)
        signUpEmailInvalidTextView = view.findViewById(R.id.signUpEmailInvalidTextView)

        if(savedInstanceState != null){
            val firstNameText = savedInstanceState.getString("firstNameText", "").toString()
            val lastNameText = savedInstanceState.getString("lastNameText", "").toString()
            val emailAddressText = savedInstanceState.getString("emailAddressText", "").toString()
            signUpFirstNameValidity = savedInstanceState.getInt("signUpFirstNameValidity", 0)
            signUpLastNameValidity = savedInstanceState.getInt("signUpLastNameValidity", 0)
            signUpEmailValidity = savedInstanceState.getInt("signUpEmailValidity", 0)

            signUpFirstNameEditText.setText(firstNameText)
            signUpLastNameEditText.setText(lastNameText)
            signUpEmailEditText.setText(emailAddressText)

            if(signUpFirstNameValidity == 1){
                signUpFirstNameEditText.setBackgroundResource(R.drawable.valid_edit_text_background)
            } else if(signUpFirstNameValidity == -1){
                signUpFirstNameEditText.setBackgroundResource(R.drawable.invalid_edit_text_background)
                signUpFirstNameInvalidTextView.visibility = View.VISIBLE
            }

            if(signUpLastNameValidity == 1){
                signUpLastNameEditText.setBackgroundResource(R.drawable.valid_edit_text_background)
            } else if(signUpLastNameValidity == -1){
                signUpLastNameEditText.setBackgroundResource(R.drawable.invalid_edit_text_background)
                signUpLastNameInvalidTextView.visibility = View.VISIBLE
            }

            if(signUpEmailValidity == 1){
                signUpEmailEditText.setBackgroundResource(R.drawable.valid_edit_text_background)
            } else if(signUpEmailValidity == -1){
                signUpEmailEditText.setBackgroundResource(R.drawable.invalid_edit_text_background)
                signUpEmailInvalidTextView.visibility = View.VISIBLE
            }
        }

        signUpFragmentOneButton.setOnClickListener {
            var validationBool = true
            val firstNameText = signUpFirstNameEditText.text.toString()
            val lastNameText = signUpLastNameEditText.text.toString()
            val emailAddressText = signUpEmailEditText.text.toString()

            if(!SignUpViewModel.validateName(firstNameText)){
                validationBool = false
                signUpFirstNameInvalidTextView.visibility = View.VISIBLE
                signUpFirstNameEditText.setBackgroundResource(R.drawable.invalid_edit_text_background)
                signUpFirstNameValidity = -1
            }else{
                signUpFirstNameEditText.setBackgroundResource(R.drawable.valid_edit_text_background)
                signUpFirstNameInvalidTextView.visibility = View.GONE
                signUpFirstNameValidity = 1
            }

            if(!SignUpViewModel.validateName(lastNameText)){
                validationBool = false
                signUpLastNameInvalidTextView.visibility = View.VISIBLE
                signUpLastNameEditText.setBackgroundResource(R.drawable.invalid_edit_text_background)
                signUpLastNameValidity = -1
            }else{
                signUpLastNameEditText.setBackgroundResource(R.drawable.valid_edit_text_background)
                signUpLastNameInvalidTextView.visibility = View.GONE
                signUpLastNameValidity = 1
            }

            if(!SignUpViewModel.validateEmail(emailAddressText)){
                validationBool = false
                signUpEmailInvalidTextView.visibility = View.VISIBLE
                signUpEmailEditText.setBackgroundResource(R.drawable.invalid_edit_text_background)
                signUpEmailValidity = -1
            }else {
                signUpEmailEditText.setBackgroundResource(R.drawable.valid_edit_text_background)
                signUpEmailInvalidTextView.visibility = View.GONE
                signUpEmailValidity = 1
            }

            if(validationBool){
                onEventListener.fragmentOneCompleted(signUpFirstNameEditText.text.toString(),
                    signUpLastNameEditText.text.toString(), signUpEmailEditText.text.toString())
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val firstNameText = signUpFirstNameEditText.text.toString()
        val lastNameText = signUpLastNameEditText.text.toString()
        val emailAddressText = signUpEmailEditText.text.toString()

        outState.putString("firstNameText", firstNameText)
        outState.putString("lastNameText", lastNameText)
        outState.putString("emailAddressText", emailAddressText)

        outState.putInt("signUpFirstNameValidity", signUpFirstNameValidity)
        outState.putInt("signUpLastNameValidity", signUpLastNameValidity)
        outState.putInt("signUpEmailValidity", signUpEmailValidity)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is OnEventListener){
            onEventListener = context
        } else {
            throw ClassCastException(context.toString()
                    + "has not implemented interface SignUpOneFragment.OnEventListener")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        println("SignUpOneFragment OnDestroy()")
    }
}