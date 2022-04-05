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
 * Use the [SignUpTwoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SignUpTwoFragment : Fragment() {

    private lateinit var onEventListener: OnEventListener
    private lateinit var signUpCityEditText: EditText
    private lateinit var signUpCityInvalidTextView: TextView
    private lateinit var signUpCountryEditText: EditText
    private lateinit var signUpCountryInvalidTextView: TextView
    private lateinit var signUpJobTitleEditText: EditText
    private lateinit var signUpJobTitleInvalidTextView: TextView
    private lateinit var signUpCompanyEditText: EditText
    private lateinit var signUpCompanyInvalidTextView: TextView

    private var signUpCityValidity = 0
    private var signUpCountryValidity = 0
    private var signUpJobTitleValidity = 0
    private var signUpCompanyValidity = 0

    interface OnEventListener {
        fun fragmentTwoCompleted(city: String, country: String, jobTitle: String, company: String)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up_two, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val signUpFragmentTwoButton = view.findViewById<Button>(R.id.signUpFragmentTwoButton)
        signUpCityEditText = view.findViewById(R.id.signUpCityEditText)
        signUpCountryEditText = view.findViewById(R.id.signUpCountryEditText)
        signUpJobTitleEditText = view.findViewById(R.id.signUpJobTitleEditText)
        signUpCompanyEditText = view.findViewById(R.id.signUpCompanyEditText)
        signUpCityInvalidTextView = view.findViewById(R.id.signUpCityInvalidTextView)
        signUpCountryInvalidTextView = view.findViewById(R.id.signUpCountryInvalidTextView)
        signUpJobTitleInvalidTextView = view.findViewById(R.id.signUpJobTitleInvalidTextView)
        signUpCompanyInvalidTextView = view.findViewById(R.id.signUpCompanyInvalidTextView)

        if(savedInstanceState != null){
            val cityText = savedInstanceState.getString("cityText", "").toString()
            val countryText = savedInstanceState.getString("countryText", "").toString()
            val jobTitleText = savedInstanceState.getString("jobTitleText", "").toString()
            val companyText = savedInstanceState.getString("companyText", "").toString()
            signUpCityValidity = savedInstanceState.getInt("signUpCityValidity", 0)
            signUpCountryValidity = savedInstanceState.getInt("signUpCountryValidity", 0)
            signUpJobTitleValidity = savedInstanceState.getInt("signUpJobTitleValidity", 0)
            signUpCompanyValidity = savedInstanceState.getInt("signUpCompanyValidity", 0)

            signUpCityEditText.setText(cityText)
            signUpCountryEditText.setText(countryText)
            signUpJobTitleEditText.setText(jobTitleText)
            signUpCompanyEditText.setText(companyText)

            if(signUpCityValidity == 1){
                signUpCityEditText.setBackgroundResource(R.drawable.valid_edit_text_background)
            } else if(signUpCityValidity == -1){
                signUpCityEditText.setBackgroundResource(R.drawable.invalid_edit_text_background)
                signUpCityInvalidTextView.visibility = View.VISIBLE
            }

            if(signUpCountryValidity == 1){
                signUpCountryEditText.setBackgroundResource(R.drawable.valid_edit_text_background)
            } else if(signUpCountryValidity == -1){
                signUpCountryEditText.setBackgroundResource(R.drawable.invalid_edit_text_background)
                signUpCountryInvalidTextView.visibility = View.VISIBLE
            }

            if(signUpJobTitleValidity == 1){
                signUpJobTitleEditText.setBackgroundResource(R.drawable.valid_edit_text_background)
            } else if(signUpJobTitleValidity == -1){
                signUpJobTitleEditText.setBackgroundResource(R.drawable.invalid_edit_text_background)
                signUpJobTitleInvalidTextView.visibility = View.VISIBLE
            }

            if(signUpCompanyValidity == 1){
                signUpCompanyEditText.setBackgroundResource(R.drawable.valid_edit_text_background)
            } else if(signUpCompanyValidity == -1){
                signUpCompanyEditText.setBackgroundResource(R.drawable.invalid_edit_text_background)
                signUpCompanyInvalidTextView.visibility = View.VISIBLE
            }
        }

        signUpFragmentTwoButton.setOnClickListener {
            var validationBool = true
            val cityText = signUpCityEditText.text.toString()
            val countryText = signUpCountryEditText.text.toString()
            val jobTitleText = signUpJobTitleEditText.text.toString()
            val companyText = signUpCompanyEditText.text.toString()

            if(!SignUpViewModel.validateCity(cityText)){
                validationBool = false
                signUpCityInvalidTextView.visibility = View.VISIBLE
                signUpCityEditText.setBackgroundResource(R.drawable.invalid_edit_text_background)
                signUpCityValidity = -1
            } else {
                signUpCityEditText.setBackgroundResource(R.drawable.valid_edit_text_background)
                signUpCityInvalidTextView.visibility = View.GONE
                signUpCityValidity = 1
            }

            if(!SignUpViewModel.validateCountry(countryText)){
                validationBool = false
                signUpCountryInvalidTextView.visibility = View.VISIBLE
                signUpCountryEditText.setBackgroundResource(R.drawable.invalid_edit_text_background)
                signUpCountryValidity = -1
            } else {
                signUpCountryEditText.setBackgroundResource(R.drawable.valid_edit_text_background)
                signUpCountryInvalidTextView.visibility = View.GONE
                signUpCountryValidity = 1
            }

            if(!SignUpViewModel.validateJobTitle(jobTitleText)){
                validationBool = false
                signUpJobTitleInvalidTextView.visibility = View.VISIBLE
                signUpJobTitleEditText.setBackgroundResource(R.drawable.invalid_edit_text_background)
                signUpJobTitleValidity = -1
            }else {
                signUpJobTitleEditText.setBackgroundResource(R.drawable.valid_edit_text_background)
                signUpJobTitleInvalidTextView.visibility = View.GONE
                signUpJobTitleValidity = 1
            }

            if(!SignUpViewModel.validateCompany(companyText)){
                validationBool = false
                signUpCompanyInvalidTextView.visibility = View.VISIBLE
                signUpCompanyEditText.setBackgroundResource(R.drawable.invalid_edit_text_background)
                signUpCompanyValidity = -1
            } else {
                signUpCompanyEditText.setBackgroundResource(R.drawable.valid_edit_text_background)
                signUpCompanyInvalidTextView.visibility = View.GONE
                signUpCompanyValidity = 1
            }

            if(validationBool){
                //Send text data to the SignUpActivity
                onEventListener.fragmentTwoCompleted(signUpCityEditText.text.toString(),
                    signUpCountryEditText.text.toString(),
                    signUpJobTitleEditText.text.toString(),
                    signUpCompanyEditText.text.toString())
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val cityText = signUpCityEditText.text.toString()
        val countryText = signUpCountryEditText.text.toString()
        val jobTitleText = signUpJobTitleEditText.text.toString()
        val companyText = signUpCompanyEditText.text.toString()

        outState.putString("cityText", cityText)
        outState.putString("countryText", countryText)
        outState.putString("jobTitleText", jobTitleText)
        outState.putString("companyText", companyText)

        outState.putInt("signUpCityValidity", signUpCityValidity)
        outState.putInt("signUpCountryValidity", signUpCountryValidity)
        outState.putInt("signUpJobTitleValidity", signUpJobTitleValidity)
        outState.putInt("signUpCompanyValidity", signUpCompanyValidity)
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