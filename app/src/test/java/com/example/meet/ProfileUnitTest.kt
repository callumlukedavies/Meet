package com.example.meet

import com.example.meet.viewmodel.ProfileViewModel
import org.junit.Assert
import org.junit.Test

class ProfileUnitTest {

    @Test
    fun validateEmailValidInput(){
        //Input is valid, expected return is true
        val input = "person@email.com"
        val result = ProfileViewModel.validateEmailAddress(input)
        Assert.assertEquals(result, true)
    }

    @Test
    fun validateEmailEmptyString(){
        //Input is empty, expected return is false
        val input = ""
        val result = ProfileViewModel.validateEmailAddress(input)
        Assert.assertEquals(result, false)
    }

    @Test
    fun validateEmailWithSpace(){
        //Input contains a space, expected return is false
        val input = "person@ email.com"
        val result = ProfileViewModel.validateEmailAddress(input)
        Assert.assertEquals(result, false)
    }

    @Test
    fun validatePasswordValidInput(){
        //Input is valid, expected return is true
        val input = "Password123?"
        val result = ProfileViewModel.validatePassword(input)
        Assert.assertEquals(result, true)
    }

    @Test
    fun validatePasswordTooShort(){
        //Input is too short, input must be at
        // least 6 characters long. Expected return is false
        val input = "Pa1?"
        val result = ProfileViewModel.validatePassword(input)
        Assert.assertEquals(result, false)
    }

    @Test
    fun validatePasswordNoNumericChar(){
        //Input does not contain a numeric char, expected return is false
        val input = "Password?"
        val result = ProfileViewModel.validatePassword(input)
        Assert.assertEquals(result, false)
    }

    @Test
    fun validatePasswordNoWhiteSpace(){
        //Input does not contain a space character, expected return is false
        val input = "Password ?123"
        val result = ProfileViewModel.validatePassword(input)
        Assert.assertEquals(result, false)
    }

    @Test
    fun validatePasswordNoSpecialChar(){
        //Input does not contain a special character, expected return is false
        val input = "Password123"
        val result = ProfileViewModel.validatePassword(input)
        Assert.assertEquals(result, false)
    }

    @Test
    fun validatePasswordNoUpperCaseChar(){
        //Input does not contain an upper case character, expected return is false
        val input = "password123?"
        val result = ProfileViewModel.validatePassword(input)
        Assert.assertEquals(result, false)
    }
}