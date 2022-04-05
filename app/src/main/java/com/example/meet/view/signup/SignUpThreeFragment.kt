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
 * Use the [SignUpThreeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SignUpThreeFragment : Fragment() {

    private lateinit var onEventListener: OnEventListener
    private lateinit var signUpPasswordEditText: EditText
    private lateinit var signUpPasswordInvalidTextView: TextView
    private lateinit var signUpConfirmPasswordEditText: EditText
    private lateinit var signUpConfirmPasswordInvalidTextView: TextView
    private var signUpPasswordValidity = 0
    private var signUpConfirmPasswordValidity = 0

    interface OnEventListener {
        fun fragmentThreeCompleted(password: String, confirmPassword: String)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up_three, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val signUpFragmentThreeButton = view.findViewById<Button>(R.id.signUpFragmentThreeButton)
        signUpPasswordEditText = view.findViewById(R.id.signUpPasswordEditText)
        signUpConfirmPasswordEditText = view.findViewById(R.id.signUpConfirmPasswordEditText)
        signUpPasswordInvalidTextView = view.findViewById(R.id.signUpPasswordInvalidTextView)
        signUpConfirmPasswordInvalidTextView = view.findViewById(R.id.signUpConfirmPasswordInvalidTextView)

        if(savedInstanceState != null){
            val passwordText = savedInstanceState.getString("passwordText", "")
            val confirmPasswordText = savedInstanceState.getString("confirmPasswordText", "")
            signUpPasswordValidity = savedInstanceState.getInt("signUpPasswordValidity")
            signUpConfirmPasswordValidity = savedInstanceState.getInt("signUpConfirmPasswordValidity")

            signUpPasswordEditText.setText(passwordText)
            signUpConfirmPasswordEditText.setText(confirmPasswordText)

            if(signUpPasswordValidity == 1){
                signUpPasswordEditText.setBackgroundResource(R.drawable.valid_edit_text_background)
            } else if(signUpPasswordValidity == -1){
                signUpPasswordEditText.setBackgroundResource(R.drawable.invalid_edit_text_background)
                signUpPasswordInvalidTextView.visibility = View.VISIBLE
            }

            if(signUpConfirmPasswordValidity == 1){
                signUpConfirmPasswordEditText.setBackgroundResource(R.drawable.valid_edit_text_background)
            } else if(signUpConfirmPasswordValidity == -1){
                signUpConfirmPasswordEditText.setBackgroundResource(R.drawable.invalid_edit_text_background)
                signUpConfirmPasswordInvalidTextView.visibility = View.VISIBLE
            }
        }

        signUpFragmentThreeButton.setOnClickListener {
            var validationBool = true
            val passwordText = signUpPasswordEditText.text.toString()
            val confirmPasswordText = signUpConfirmPasswordEditText.text.toString()

            if(!SignUpViewModel.validatePassword(passwordText)){
                validationBool = false
                signUpPasswordInvalidTextView.visibility = View.VISIBLE
                signUpPasswordEditText.setBackgroundResource(R.drawable.invalid_edit_text_background)
                signUpPasswordValidity = -1
            } else {
                signUpPasswordEditText.setBackgroundResource(R.drawable.valid_edit_text_background)
                signUpPasswordInvalidTextView.visibility = View.GONE
                signUpPasswordValidity = 1
            }


            if(!SignUpViewModel.matchPassword(passwordText, confirmPasswordText)){
                validationBool = false
                signUpConfirmPasswordInvalidTextView.visibility = View.VISIBLE
                signUpConfirmPasswordEditText.setBackgroundResource(R.drawable.invalid_edit_text_background)
                signUpConfirmPasswordValidity = -1
            } else {
                signUpConfirmPasswordEditText.setBackgroundResource(R.drawable.valid_edit_text_background)
                signUpConfirmPasswordInvalidTextView.visibility = View.GONE
                signUpConfirmPasswordValidity = 1
            }

            if(validationBool){
                onEventListener.fragmentThreeCompleted(signUpPasswordEditText.text.toString(),
                    signUpConfirmPasswordEditText.text.toString())
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val passwordText = signUpPasswordEditText.text.toString()
        val confirmPasswordText = signUpConfirmPasswordEditText.text.toString()

        outState.putString("passwordText", passwordText)
        outState.putString("confirmPasswordText", confirmPasswordText)
        outState.putInt("signUpPasswordValidity", signUpPasswordValidity)
        outState.putInt("signUpConfirmPasswordValidity", signUpConfirmPasswordValidity)
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
}