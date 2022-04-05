package com.example.meet.model

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.messaging.FirebaseMessaging

class MainRepository() {
    private var firebaseAuth : FirebaseAuth
    private var database : DatabaseReference
    private lateinit var eventChatCallback: EventChatCallback
    private lateinit var eventChatListener: ChildEventListener

    interface EventChatCallback {
//        fun onNewMessage(list: MutableList<HashMap<String, String>>)

        fun onNewMessage(key: String, message: HashMap<String, String>)

        fun onError(exception: Exception)
    }

    init {
        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance("https://meet-32ba7-default-rtdb.europe-west1.firebasedatabase.app/").reference
    }

    fun addEventChatCallback(eventId: String, callback: EventChatCallback){
        eventChatCallback = callback
        val chatListener = object : ChildEventListener {

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                if(snapshot.value != null){
                    val key = snapshot.key as String
                    val message = snapshot.value as java.util.HashMap<String, String>

                    println("new message: ${message["time"]}")
                    eventChatCallback.onNewMessage(key, message)

                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("MainRepository", "Error occurred in database. ${error.toString()}")
            }
        }

        eventChatListener = database.child("Events/$eventId/messages/").addChildEventListener(chatListener)

//        val chatListener = object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                Log.d("MainRepository", "New message added to event chat")
//                if(snapshot.value != null){
//                    val messages = snapshot.value as MutableList<java.util.HashMap<String, String>>
//                    eventChatCallback.onNewMessage(messages)
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                Log.d("MainRepository", "Error occurred in database. ${error.toString()}")
//            }
//        }
//
//        eventChatListener = database.child("Events/$eventId/messages/").addValueEventListener(chatListener)
    }

    fun removeEventChatCallback() {
        database.removeEventListener(eventChatListener)
        Log.d("MainRepository", "eventChatListener has been removed")
    }

    fun login(email : String, password: String) : Task<AuthResult> {
        return firebaseAuth.signInWithEmailAndPassword(email, password)
    }

    fun subscribeToNotifications(): Task<Void> {
        return FirebaseMessaging.getInstance().subscribeToTopic(firebaseAuth.uid.toString())
    }

    fun unsubscribeFromNotifications(): Task<Void> {
        return FirebaseMessaging.getInstance().unsubscribeFromTopic(firebaseAuth.uid.toString())
    }

    fun signUp(email : String, password : String) : Task<AuthResult> {
        return firebaseAuth.createUserWithEmailAndPassword(email, password)
    }

    fun sendUserDataToDatabase(map : HashMap<String, Any>) : Task<Void> {
        return database.child("Users").child(firebaseAuth.uid.toString()).setValue(map)
    }

    fun getMyUserData() :  Task<DataSnapshot> {
        return database.child("Users").child(firebaseAuth.uid!!).get()
    }

    fun getUserData(uid : String) :Task<DataSnapshot> {
        return database.child("Users").child(uid).get()
    }

    fun getAllUsers(): Task<DataSnapshot> {
        return database.child("Users").get()
    }

    fun changeEmail(newEmail : String) : Task<Void> {
        return firebaseAuth.currentUser!!.updateEmail(newEmail)
    }

    fun changePassword(newPassword : String) : Task<Void> {
        return firebaseAuth.currentUser!!.updatePassword(newPassword)
    }

    fun updateFriendsList(friendsList : MutableList<String>): Task<Void> {
        return database.child("Users").child(firebaseAuth.uid!!).child("friends").setValue(friendsList)
    }

    fun sendFriendRequest(friendId : String, friendsList: MutableList<String>): Task<Void> {
        return database.child("Users").child(friendId).child("friends").setValue(friendsList)
    }

    fun getFriendsOfUser(friendId : String): Task<DataSnapshot> {
        return database.child("Users").child(friendId).child("friends").get()
    }

    fun createEvent(map : HashMap<String, Any>): Task<Void> {
        return database.child("Events").child(map["eventId"] as String).setValue(map)
    }

    fun sendInvitation(userId: String, invitations: MutableList<String>) : Task<Void> {
        return database.child("Users").child(userId).child("invitations").setValue(invitations)
    }

    fun getInvitations(userId: String) : Task<DataSnapshot> {
        return database.child("Users").child(userId).child("invitations").get()
    }

    fun getEventDetails(eventId: String): Task<DataSnapshot> {
        return database.child("Events").child(eventId).get()
    }

    fun getAttendingList(eventId: String): Task<DataSnapshot> {
        //Return the list of users attending the event
        return database.child("Events").child(eventId).child("attendingList").get()
    }

    fun acceptInvitation(eventId: String, attendingList: MutableList<String>): Task<Void> {
        //Update the attendingList variable for the event
        return database.child("Events").child(eventId).child("attendingList").setValue(attendingList)
    }

    fun updateInvitedList(eventId: String, invitedList: MutableList<String>): Task<Void> {
        return database.child("Events").child(eventId).child("invitedList").setValue(invitedList)
    }

    fun deleteInvitation(eventId: String, userId: String): Task<Void> {
        //Delete an invitation from the invited list
        return database.child("Events").child(eventId).child("invitedList").child(userId).removeValue()
    }

    fun removeInvitation(eventId: String): Task<Void> {
        //Is this correct???
        return database.child("Users").child(firebaseAuth.uid!!).child("invitations").removeValue()
    }

    fun setUsersEvents(events: MutableList<String>): Task<Void> {
        return database.child("Users").child(firebaseAuth.uid!!).child("events").setValue(events)
    }

    fun getUsersEvents(): Task<DataSnapshot> {
        return database.child("Users").child(firebaseAuth.uid!!).child("events").get()
    }

    fun sendMessageToEventChat(message: HashMap<String, String>, eventId: String): Task<Void> {
        return database.child("Events").child(eventId).child("messages").push().setValue(message)
    }

}