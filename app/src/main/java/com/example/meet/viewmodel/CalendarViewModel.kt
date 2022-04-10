package com.example.meet.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.meet.model.Event
import com.example.meet.model.MainRepository
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*

class CalendarViewModel(application: Application) : AndroidViewModel(application) {

    private var mainRepository : MainRepository
    private var firebaseAuth : FirebaseAuth
    private var events: MutableList<Event>
    private var eventsMutableLiveData: MutableLiveData<MutableList<Event>>

    init {
        mainRepository = MainRepository()
        firebaseAuth = FirebaseAuth.getInstance()
        events = mutableListOf()
        eventsMutableLiveData = MutableLiveData()
        getEvents()
    }

    fun getEvents(){
        mainRepository.getUsersEvents().addOnCompleteListener {
            if(it.isSuccessful && it.result!!.value != null){
                events = mutableListOf()
                val eventIds = it.result!!.value as MutableList<String>
                getEventDetails(eventIds)
            }
        }
    }

    private fun getEventDetails(eventIds: MutableList<String>){
        val calendar = Calendar.getInstance(Locale.ENGLISH)
        val dateFormat = SimpleDateFormat("dd/MM/yyyy hh:mm", Locale.ENGLISH)

        for(eventId: String in eventIds){
            if(eventId != null) mainRepository.getEventDetails(eventId).addOnCompleteListener {
                if(it.isSuccessful && it.result!!.value != null){
                    Log.d("Firebase Comms", "Successfully received event data")
                    val date = it.result!!.child("date").value.toString()
                    val time = it.result!!.child("time").value.toString()
                    val eventDate = "$date $time"

                    //Check if today's date is after the event, if true don't add to list
                    if(calendar.time.after(dateFormat.parse(eventDate))){
                        return@addOnCompleteListener
                    }
                    //check if messages exist for this event
                    val messages = if(it.result!!.child("messages").value == null) HashMap()
                    else it.result!!.child("messages").value as HashMap<String, HashMap<String, String>>

                    //check if invited list exists for event
                    val invitedList = if(it.result!!.child("invitedList").value == null) mutableListOf<String>()
                    else it.result.child("invitedList").value as MutableList<String>

                    //add event to the list
                    events.add(
                        Event(it.result!!.child("name").value.toString(),
                            it.result!!.child("date").value.toString(),
                            it.result!!.child("time").value.toString(),
                            it.result!!.child("placeName").value.toString(),
                            it.result!!.child("placeId").value.toString(),
                            it.result!!.child("eventId").value.toString(),
                            it.result!!.child("organiserId").value.toString(),
                            it.result!!.child("description").value.toString(),
                            invitedList,
                            it.result!!.child("attendingList").value as MutableList<String>,
                            messages
                        )
                    )
                    events.sortBy { event ->
                        event.eventDate
                    }

                    eventsMutableLiveData.postValue(events)
                }else {
                    Log.d("Firebase Comms", "Could not receive event data")
                }
            }
        }
    }

    fun removeEventFromList(eventId: String): MutableList<Event> {
        for(event: Event in events){
            if(eventId == event.eventId){
                events.remove(event)
                break
            }
        }

        return events
    }

    fun getEventsMutableLiveData(): MutableLiveData<MutableList<Event>> {
        return eventsMutableLiveData
    }
}