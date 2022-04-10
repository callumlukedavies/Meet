package com.example.meet.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.meet.model.Event
import com.example.meet.model.MainRepository
import com.example.meet.model.Message
import com.example.meet.model.User
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*

class ViewEventViewModel(application: Application, eventId: String) : AndroidViewModel(application),
    MainRepository.EventChatCallback {

    private var firebaseAuth : FirebaseAuth
    private var mainRepository : MainRepository
    private var eventMutableLiveData: MutableLiveData<Event>
    private var invitedListMutableLiveData: MutableLiveData<MutableList<User>>
    private var attendingListMutableLiveData: MutableLiveData<MutableList<User>>
    private var messagesListMutableLiveData: MutableLiveData<MutableList<Message>>
    private var isUserAttendingMutableLiveData: MutableLiveData<Boolean>
    private var invitedListOfUsers: MutableList<User>
    private var attendingListOfUsers: MutableList<User>
    private var messagesList: MutableList<Message>
    private lateinit var event: Event
    private lateinit var userName: String
    private var isUserAttending = false
    private var eventChatCallback: MainRepository.EventChatCallback

    init {
        firebaseAuth = FirebaseAuth.getInstance()
        mainRepository = MainRepository()
        isUserAttendingMutableLiveData = MutableLiveData()
        isUserAttendingMutableLiveData.postValue(false)
        invitedListMutableLiveData = MutableLiveData()
        invitedListOfUsers = mutableListOf()
        attendingListOfUsers = mutableListOf()
        messagesList = mutableListOf()
        attendingListMutableLiveData = MutableLiveData()
        eventMutableLiveData = MutableLiveData()
        messagesListMutableLiveData = MutableLiveData()
        getEventFromDatabase(eventId)
        getUserNameFromDatabase()
        eventChatCallback = this
        mainRepository.addEventChatCallback(eventId, eventChatCallback)

    }

    fun removeEventChatCallback(){
        mainRepository.removeEventChatCallback()
    }

    private fun getUserNameFromDatabase() {
        mainRepository.getMyUserData().addOnCompleteListener {
            if (it.isSuccessful) {
                Log.d("firebase Communications", "Get user name from database: Successful")
                val firstName = it.result!!.child("firstName").value.toString()
                val lastName = it.result!!.child("lastName").value.toString()
                userName = "$firstName $lastName"
            } else {
                Log.d("firebase Communications", "Get user name from database: Unsuccessful")
            }
        }
    }

    private fun getEventFromDatabase(eventId: String){
        mainRepository.getEventDetails(eventId).addOnCompleteListener {
            if(it.isSuccessful && it.result!!.value != null){
                Log.d("Firebase Comms", "Successfully received event data")

                val messages = if(it.result!!.child("messages").value == null) HashMap()
                else it.result!!.child("messages").value as HashMap<String, HashMap<String, String>>

                //check if invited list exists for event
                val invitedList = if(it.result!!.child("invitedList").value == null) mutableListOf<String>()
                else it.result.child("invitedList").value as MutableList<String>

                event = Event(it.result!!.child("name").value.toString(),
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

                eventMutableLiveData.postValue(event)
                if(it.result!!.child("invitedList").value != null){
                    //idType = 0 signals these users are invited
                    invitedListOfUsers = processIds(event.invitedList, 0)
                    invitedListMutableLiveData.postValue(invitedListOfUsers)
                }

                if(it.result!!.child("attendingList").value != null){
                    //idType = 1 signals these users are attending
                    attendingListOfUsers = processIds(event.attendingList, 1)
                    attendingListMutableLiveData.postValue(attendingListOfUsers)
                }

            }else {
                Log.d("Firebase Comms", "Could not receive event data")
            }
        }
    }

    fun getEvent(): MutableLiveData<Event> {
        return eventMutableLiveData
    }

    fun processIds(friendIdList : MutableList<String>, idType: Int) : MutableList<User>{
        val list = mutableListOf<User>()
        for(id : String in friendIdList){
            mainRepository.getUserData(id).addOnCompleteListener{
                if(it.isSuccessful){
                    val firstName = it.result!!.child("firstName").getValue().toString()
                    val lastName = it.result!!.child("lastName").getValue().toString()
                    list.add(User(firstName, lastName, id))
                    if(id == firebaseAuth.uid && idType == 1){
                        println("User is attending event!")
                        isUserAttending = true
                        isUserAttendingMutableLiveData.postValue(isUserAttending)
                    }
                } else {
                    Log.d("firebase comms: ", "Unable to receive user data.")
                }
            }
        }
        return list
    }

    //Override EventChatCallback method onNewMessage
    override fun onNewMessage(key: String, message: HashMap<String, String>) {

        Log.d("ViewEventViewModel","new message: ${message["time"]}")
        messagesList.add(Message(message["userName"]!!,
            message["time"]!!, message["message"]!!, message["userId"]!!))
        messagesListMutableLiveData.postValue(messagesList)
    }

    //Override EventChatCallback method onError
    override fun onError(exception: Exception) {
        Log.d("ViewEventViewModel", "Error getting new message: $exception")
    }

    fun processMessages(messagesList: HashMap<String, HashMap<String, String>>): MutableList<Message> {
        val messages = mutableListOf<Message>()
        for((key, value) in messagesList){
            if(messagesList[key] != null) {
                messages.add(
                    Message(
                        messagesList[key]?.get("userName")!!,
                        messagesList[key]?.get("time")!!,
                        messagesList[key]?.get("message")!!,
                        messagesList[key]?.get("userId")!!
                    )
                )
            }
        }
        return messages
    }

    fun getInvitedListMutableLiveData(): MutableLiveData<MutableList<User>> {
        return invitedListMutableLiveData
    }

    fun getAttendingListMutableLiveData(): MutableLiveData<MutableList<User>> {
        return attendingListMutableLiveData
    }

    fun getMessagesListMutableLiveData(): MutableLiveData<MutableList<Message>> {
        return messagesListMutableLiveData
    }

    fun sendMessageToEventChat(textMessage: String){
        val milliseconds = System.currentTimeMillis()
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = milliseconds
        val dateFormat = SimpleDateFormat("HH:mm", Locale.ENGLISH)
        val time = dateFormat.format(calendar.time)

        val messageObject: HashMap<String, String> = HashMap()

        messageObject["message"] = textMessage
        messageObject["userName"] = userName
        messageObject["time"] = time
        messageObject["userId"] = firebaseAuth.currentUser!!.uid
//        event.eventMessages.add(messageObject)
//        messagesList.add(Message(userName, time, textMessage, firebaseAuth.currentUser!!.uid))
//        messagesListMutableLiveData.postValue(messagesList)

        mainRepository.sendMessageToEventChat(messageObject, event.eventId).addOnCompleteListener {
            if(it.isSuccessful){
                Log.d("firebase comms: ", "User successfully sent a message")
            } else {
                Log.d("firebase comms: ", "User could not send a message")
            }
        }
    }

    fun acceptInvitation(){
        event.invitedList.remove(firebaseAuth.uid)
        firebaseAuth.uid?.let { event.attendingList.add(it) }
        mainRepository.acceptInvitation(event.eventId, event.attendingList).addOnCompleteListener {
            if(it.isSuccessful){
                Log.d("firebase comms: ", "User successfully accepted invitation")
            } else {
                Log.d("firebase comms: ", "User could not accept invitation")
            }
        }

        mainRepository.updateInvitedList(event.eventId, event.invitedList).addOnCompleteListener {
            if(it.isSuccessful){
                Log.d("firebase comms: ", "User successfully removed from invited list")
            } else {
                Log.d("firebase comms: ", "User not successfully removed from invited list")
            }
        }

        mainRepository.removeInvitation(event.eventId).addOnCompleteListener {
            if(it.isSuccessful){
                Log.d("firebase comms: ", "User successfully removed invitation")
            } else {
                Log.d("firebase comms: ", "User could not remove invitation from list")
            }
        }

        mainRepository.getUsersEvents().addOnCompleteListener {
            if(it.isSuccessful){
                Log.d("firebase comms", "successfully received users events.")
                val events = if(it.result!!.value != null) it.result!!.value as MutableList<String>
                else mutableListOf()
                events.add(event.eventId)
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

    fun rejectInvitation(){
        event.invitedList.remove(firebaseAuth.uid)

        mainRepository.updateInvitedList(event.eventId, event.invitedList).addOnCompleteListener {
            if(it.isSuccessful){
                Log.d("firebase comms: ", "User successfully removed from invited list")
            } else {
                Log.d("firebase comms: ", "User not successfully removed from invited list")
            }
        }

        mainRepository.removeInvitation(event.eventId).addOnCompleteListener {
            if(it.isSuccessful){
                Log.d("firebase comms: ", "User successfully removed invitation")
            } else {
                Log.d("firebase comms: ", "User could not remove invitation from list")
            }
        }
    }

    fun getFirebaseAuthUID(): String {
        return firebaseAuth.uid.toString()
    }

    fun getIsUserAttendingMutableLiveData(): MutableLiveData<Boolean> {
        return isUserAttendingMutableLiveData
    }

    fun getEventPlaceId(): String {
        return event.eventPlaceId
    }
}