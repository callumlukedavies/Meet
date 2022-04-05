package com.example.meet

import com.example.meet.viewmodel.SignUpViewModel
import org.junit.Assert.assertEquals
import org.junit.Test

class SignUpUnitTest {
    @Test
    fun validateFirstNameValidInput(){
        //input is valid, expected return is true
        val input = "firstname"
        val result = SignUpViewModel.validateName(input)
        assertEquals(result, true)
    }

    @Test
    fun validateFirstNameNumericChar(){
        //input contains a numeric char, expected return is false
        val input = "first5name"
        val result = SignUpViewModel.validateName(input)
        assertEquals(result, false)
    }

    @Test
    fun validateFirstNameEmptyString(){
        //Input is an empty string, expected return is false
        val input = ""
        val result = SignUpViewModel.validateName(input)
        assertEquals(result, false)
    }

    @Test
    fun validateFirstNameIncludeSpace(){
        //Input contains spaces, expected return is false
        val input = "first name"
        val result = SignUpViewModel.validateName(input)
        assertEquals(result, false)
    }

    @Test
    fun validateFirstNameSpecialChars(){
        //Input contains special characters, expected return is false
        val input = "firstn@me"
        val result = SignUpViewModel.validateName(input)
        assertEquals(result, false)
    }

    @Test
    fun validateLastNameValidInput(){
        //input is valid, expected return is true
        val input = "lastname"
        val result = SignUpViewModel.validateName(input)
        assertEquals(result, true)
    }

    @Test
    fun validateLastNameNumericChar(){
        //input contains a numeric char, expected return is false
        val input = "last5name"
        val result = SignUpViewModel.validateName(input)
        assertEquals(result, false)
    }

    @Test
    fun validateLastNameEmptyString(){
        //Input is an empty string, expected return is false
        val input = ""
        val result = SignUpViewModel.validateName(input)
        assertEquals(result, false)
    }

    @Test
    fun validateLastNameIncludeSpace(){
        //Input contains spaces, expected return is false
        val input = "last name"
        val result = SignUpViewModel.validateName(input)
        assertEquals(result, false)
    }

    @Test
    fun validateLastNameSpecialChars(){
        //Input contains special characters, expected return is false
        val input = "lastn@me"
        val result = SignUpViewModel.validateName(input)
        assertEquals(result, false)
    }

    @Test
    fun validateEmailValidInput(){
        //Input is valid, expected return is true
        val input = "person@email.com"
        val result = SignUpViewModel.validateEmail(input)
        assertEquals(result, true)
    }

    @Test
    fun validateEmailEmptyString(){
        //Input is empty string, expected return is false
        val input = ""
        val result = SignUpViewModel.validateEmail(input)
        assertEquals(result, false)
    }

    @Test
    fun validateEmailIncludesSpace(){
        //Input contains space, expected return is false
        val input = "per son@email.com"
        val result = SignUpViewModel.validateEmail(input)
        assertEquals(result, false)
    }

    @Test
    fun validateEmailNoAtSymbol(){
        //Input does not contain @ symbol, expected return is false
        val input = "personemail.com"
        val result = SignUpViewModel.validateEmail(input)
        assertEquals(result, false)
    }

//    @Test
//    fun validateEmailInvalidDomainName(){
//        //Input has invalid email domain name, expected return is false
//        val input = "person@email.55com"
//        val result = SignUpViewModel.validateEmail(input)
//        assertEquals(result, false)
//    }

    @Test
    fun validateEmailSpecialSymbol(){
        //Input contains special character, expected return is false
        val input = "person!email.com"
        val result = SignUpViewModel.validateEmail(input)
        assertEquals(result, false)
    }

    @Test
    fun validatePasswordValidInput(){
        //Input is valid, expected return is true
        val input = "Password123?"
        val result = SignUpViewModel.validatePassword(input)
        assertEquals(result, true)
    }

    @Test
    fun validatePasswordTooShort(){
        //Input is too short, input must be at
        // least 6 characters long. Expected return is false
        val input = "Pa1?"
        val result = SignUpViewModel.validatePassword(input)
        assertEquals(result, false)
    }

    @Test
    fun validatePasswordNoNumericChar(){
        //Input does not contain a numeric char, expected return is false
        val input = "Password?"
        val result = SignUpViewModel.validatePassword(input)
        assertEquals(result, false)
    }

    @Test
    fun validatePasswordNoSpecialChar(){
        //Input does not contain a special character, expected return is false
        val input = "Password123"
        val result = SignUpViewModel.validatePassword(input)
        assertEquals(result, false)
    }

    @Test
    fun validatePasswordNoUpperCaseChar(){
        //Input does not contain an upper case character, expected return is false
        val input = "password123?"
        val result = SignUpViewModel.validatePassword(input)
        assertEquals(result, false)
    }

    @Test
    fun validateMatchingPasswordFields(){
        //Inputs match, expected return is true
        val inputOne = "Password123?"
        val inputTwo = "Password123?"
        val result = SignUpViewModel.matchPassword(inputOne, inputTwo)
        assertEquals(result, true)
    }

    @Test
    fun validateMatchingPasswordsInvalidMatch(){
        //Inputs do not match, expected return is false
        val inputOne = "Password123?"
        val inputTwo = "Password133?"
        val result = SignUpViewModel.matchPassword(inputOne, inputTwo)
        assertEquals(result, false)
    }
}