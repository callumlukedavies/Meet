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

class FeedViewModel(application: Application): AndroidViewModel(application) {

    private var mainRepository: MainRepository
    private var firebaseAuth : FirebaseAuth
    private var invitationsMutableLiveData: MutableLiveData<MutableList<Event>>
    private var invitations: MutableList<Event>

    init {
        mainRepository = MainRepository()
        firebaseAuth = FirebaseAuth.getInstance()
        invitationsMutableLiveData = MutableLiveData()
        invitations = mutableListOf()
        getInvitations()
    }

    fun getInvitations() {
        mainRepository.getInvitations(firebaseAuth.uid.toString()).addOnCompleteListener {
            if(it.isSuccessful && it.result!!.value != null){
                val eventIds = it.result!!.value as MutableList<String>
                getEventDetails(eventIds)
            }
        }
    }

    private fun getEventDetails(eventIds: MutableList<String>){
        val calendar = Calendar.getInstance(Locale.ENGLISH)
        val dateFormat = SimpleDateFormat("dd/MM/yyyy hh:mm", Locale.ENGLISH)


        if(eventIds.size > 0){
            for(eventId: String in eventIds){
                mainRepository.getEventDetails(eventId).addOnCompleteListener { it ->
                    if(it.isSuccessful && it.result!!.value != null){
                        val date = it.result!!.child("date").value.toString()
                        val time = it.result!!.child("time").value.toString()
                        val eventDate = "$date $time"
                        //check if event date is before current date, if true don't add to list
                        if(calendar.time.after(dateFormat.parse(eventDate))){
                            return@addOnCompleteListener
                        }

                        Log.d("Firebase Comms", "Successfully received event data")
                        val messages = if(it.result!!.child("messages").value == null) HashMap()
                        else it.result!!.child("messages").value as HashMap<String, HashMap<String, String>>

                        //check if invited list exists for event
                        val invitedList = if(it.result!!.child("invitedList").value == null) mutableListOf()
                        else it.result.child("invitedList").value as MutableList<String>

                        invitations.add(Event(it.result!!.child("name").value.toString(),
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
                        ))
                        invitationsMutableLiveData.postValue(invitations)
                    } else {
                        Log.d("Firebase Comms", "Could not receive event data")
                    }
                }
            }
        }
    }

    fun getInvitationsMutableLiveData(): MutableLiveData<MutableList<Event>> {
        return invitationsMutableLiveData
    }

    fun removeEventFromList(eventId: String): MutableList<Event> {
        for(invitation: Event in invitations){
            if(eventId == invitation.eventId){
                invitations.remove(invitation)
                break
            }
        }
        return invitations
    }
}