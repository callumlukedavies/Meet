package com.example.meet

import com.example.meet.viewmodel.EventsViewModel
import org.junit.Assert
import org.junit.Test

class EventUnitTest {

    @Test
    fun validateEventNameValidInput(){
        //Input is valid, expected return is true
        val input = "Coffee with friends"
        val result = EventsViewModel.validateEventName(input)
        Assert.assertEquals(result, true)
    }

    @Test
    fun validateEventNameBlankInput(){
        //Input is blank, expected return is false
        val input = "   "
        val result = EventsViewModel.validateEventName(input)
        Assert.assertEquals(result, false)
    }

    @Test
    fun validateEventNameSingleSymbolInput(){
        //Input is valid, expected return is false
        val input = "@"
        val result = EventsViewModel.validateEventName(input)
        Assert.assertEquals(result, false)
    }

    @Test
    fun validateEventNameSingleCharInput(){
        //Input is valid, expected return is false
        val input = "A"
        val result = EventsViewModel.validateEventName(input)
        Assert.assertEquals(result, false)
    }

    @Test
    fun validateEventNameSingleNumericalCharInput(){
        //Input is valid, expected return is false
        val input = "5"
        val result = EventsViewModel.validateEventName(input)
        Assert.assertEquals(result, false)
    }

    @Test
    fun validateEventNameEmptyInput(){
        //Input is valid, expected return is false
        val input = ""
        val result = EventsViewModel.validateEventName(input)
        Assert.assertEquals(result, false)
    }
}