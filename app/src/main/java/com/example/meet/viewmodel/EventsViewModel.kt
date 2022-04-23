package com.example.meet.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.meet.model.MainRepository
import com.example.meet.model.User
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import java.util.*

class EventsViewModel(application: Application) : AndroidViewModel(application) {
    private var mainRepository : MainRepository
    private var firebaseAuth : FirebaseAuth
    private lateinit var eventName: String
    private lateinit var eventTime: String
    private lateinit var eventDate: String
    private lateinit var eventDescription: String
    private lateinit var eventPlaceId: String
    private lateinit var eventPlaceName: String
    private lateinit var eventId: String
    private var userFriendsList: MutableList<User>
    private var invitedList: MutableList<String>
    private var attendingList: MutableList<String>

    init {
        mainRepository = MainRepository()
        firebaseAuth = FirebaseAuth.getInstance()
        userFriendsList = mutableListOf()
        attendingList = mutableListOf()
        invitedList = mutableListOf()
        getFriendsList()
    }

    fun createEvent() = viewModelScope.launch {
        //Creates an event object, passes it to the main repository, and then makes a call
        // to the main repository to send out invitations to each user on the invitedList
        val hashMap: HashMap<String, Any> = HashMap()
        eventId = UUID.randomUUID().toString()
        firebaseAuth.uid?.let { attendingList.add(it) }
        hashMap.put("name", eventName)
        hashMap.put("date", eventDate)
        hashMap.put("time", eventTime)
        hashMap.put("description", eventDescription)
        hashMap.put("organiserId", firebaseAuth.uid!!)
        hashMap.put("invitedList", invitedList)
        hashMap.put("attendingList", attendingList)
        hashMap.put("eventId", eventId)
        hashMap.put("messages", HashMap<String, HashMap<String, String>>())
        hashMap.put("placeName", eventPlaceName)
        hashMap.put("placeId", eventPlaceId)

        mainRepository.createEvent(hashMap).addOnCompleteListener {
            if(it.isSuccessful){
                Log.d("firebase comms", "Successfully created the event.")
            } else{
                Log.d("firebase comms", "Unable to create the event.")
            }
        }

        for(userId: String in invitedList){
            mainRepository.sendInvitation(userId, eventId).addOnCompleteListener {
                if(it.isSuccessful){
                    Log.d("firebase comms", "Successfully sent event invitation to $userId.")
                } else{
                    Log.d("firebase comms", "Unable to send event invitation to $userId.")
                }
            }
        }

        mainRepository.addNewEventToUserEvents(eventId).addOnCompleteListener {
            if(it.isSuccessful){
                Log.d("firebase comms", "Successfully added new event to users events.")
            } else{
                Log.d("firebase comms", "Unable to add new event to users events.")
            }
        }
    }

    private fun getFriendsList() = viewModelScope.launch {
        //Get a list of the users friends from the database and store it
        //in a MutableLiveData object
        var friendIdList : HashMap<String, String>
        mainRepository.getMyUserData().addOnCompleteListener {
            if(it.isSuccessful){
                Log.d("firebase comms: ", "Retrieved friends from database")
                if(it.result!!.child("friends").getValue() != null){
                    friendIdList = it.result!!.child("friends").getValue() as HashMap<String, String>
                    userFriendsList = processIds(friendIdList)
                }
            }else {
                Log.d("firebase comms: ", "Unable to get friends ID list.")
            }
        }
    }

    private fun processIds(friendIdList : HashMap<String, String>) : MutableList<User>{
        //Iterate through the friendId list and create a User object with each Id
        val list = mutableListOf<User>()
        for((key, value) in friendIdList){
            mainRepository.getUserData(friendIdList[key].toString()).addOnCompleteListener{
                if(it.isSuccessful){
                    var firstName = it.result!!.child("firstName").getValue().toString()
                    var lastName = it.result!!.child("lastName").getValue().toString()
                    list.add(User(firstName, lastName, friendIdList[key].toString(), null))
                } else {
                    Log.d("firebase comms: ", "Unable to receive user data.")
                }
            }
        }
        return list
    }

    fun setEventName(name: String){
        eventName = name
    }

    fun setEventDate(date: String){
        eventDate = date
    }

    fun setEventTime(time: String){
        eventTime = time
    }

    fun setEventPlaceName(name: String){
        eventPlaceName = name
    }

    fun setEventPlaceId(id: String){
        eventPlaceId = id
    }

    fun setEventDescription(description: String){
        eventDescription = description
    }

    fun setInvitedList(invited: MutableList<String>){
        invitedList = invited
    }

    fun getUserFriendsList() : MutableList<User>{
        return userFriendsList
    }

    companion object {
        fun validateEventName(eventName: String) : Boolean {
            return !(eventName.isBlank() || eventName.length < 2)
        }
    }
}