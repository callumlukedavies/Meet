package com.example.meet.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.example.meet.model.MainRepository
import com.example.meet.model.User
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
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

    fun createEvent() {
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

        mainRepository.createEvent(hashMap)

        for(userId: String in invitedList){
            mainRepository.getInvitations(userId).addOnCompleteListener {
                if(it.isSuccessful){
                    val tempInvitations =
                    if(it.result!!.value != null) it.result!!.value as MutableList<String>
                    else mutableListOf()

                    tempInvitations.add(eventId)
                    mainRepository.sendInvitation(userId, tempInvitations)
                }
            }
        }

        mainRepository.getUsersEvents().addOnCompleteListener {
            if(it.isSuccessful){
                Log.d("firebase comms", "successfully received users events.")
                val events = if(it.result!!.value != null) it.result!!.value as MutableList<String>
                else mutableListOf()
                events.add(eventId)
                mainRepository.setUsersEvents(events).addOnCompleteListener {
                    if(it.isSuccessful){
                        Log.d("firebase comms", "successfully added event to users events.")
                    }else{
                        Log.d("firebase comms", "Unable to add event to users events.")
                    }
                }
            } else{
                Log.d("firebase comms", "Unable to get users events.")
            }
        }
    }

    fun getFriendsList() {
        //Get a list of the users friends from the database and store it
        //in a MutableLiveData object
        var friendIdList : MutableList<String>

        runBlocking {
            launch(Dispatchers.Default){
                mainRepository.getMyUserData().addOnCompleteListener {
                    if(it.isSuccessful){
                        Log.d("firebase comms: ", "Retrieved friends from database")
                        if(it.result!!.child("friends").getValue() != null){
                            friendIdList = it.result!!.child("friends").getValue() as MutableList<String>
                            userFriendsList = processIds(friendIdList)
                        }
                    }else {
                        Log.d("firebase comms: ", "Unable to get friends ID list.")
                    }
                }
            }
        }

    }

    fun processIds(friendIdList : MutableList<String>) : MutableList<User>{
        val list = mutableListOf<User>()
        for(id : String in friendIdList){
            mainRepository.getUserData(id).addOnCompleteListener{
                if(it.isSuccessful){
                    var firstName = it.result!!.child("firstName").getValue().toString()
                    var lastName = it.result!!.child("lastName").getValue().toString()
                    list.add(User(firstName, lastName, id))
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