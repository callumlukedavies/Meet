package com.example.meet.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.meet.model.MainRepository
import com.example.meet.model.User
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class FriendsViewModel(application: Application) : AndroidViewModel(application) {
    private var firebaseAuth : FirebaseAuth
    private var mainRepository : MainRepository

    private var friendsListMutableLiveData : MutableLiveData<MutableList<User>>
    private var requestedListMutableLiveData : MutableLiveData<MutableList<User>>
    private var usersListAdapterMutableLiveData : MutableLiveData<MutableList<User>>

    //List of the users friends Id's
    private var userFriendsList : MutableList<User>
    //List of the users friend request's Id's
    private var userRequestedList : MutableList<User>
    //List of all users on the platform
    private var listOfUsers : MutableList<User>
    //list of users sent friend request's Id's
    private var sentFriendRequestsList : MutableList<User>

    init {
        firebaseAuth = FirebaseAuth.getInstance()
        mainRepository = MainRepository()
        friendsListMutableLiveData = MutableLiveData()
        requestedListMutableLiveData = MutableLiveData()
        usersListAdapterMutableLiveData = MutableLiveData()
        userFriendsList = mutableListOf()
        userRequestedList = mutableListOf()
        listOfUsers = mutableListOf()
        sentFriendRequestsList = mutableListOf()

        friendsListMutableLiveData.postValue(userFriendsList)
        requestedListMutableLiveData.postValue(userRequestedList)
        getUserData()
    }

    fun searchFriendsList(name : String): MutableList<User> {
        //Searches the userFriendsList variable for a friend with a name
        //matching the input and returns the list of results that match
        val possibleResultsList = mutableListOf<User>()
        for(friend: User in userFriendsList){
            if((friend.firstName + " " + friend.lastName).lowercase().contains(name.lowercase())){
                possibleResultsList.add(friend)
            }
        }

        return possibleResultsList
    }

    fun searchUserList(name : String): MutableList<User> {
        //Searches the userFriendsList variable for a user with a name
        //matching the input and returns the list of results that match
        val possibleResultsList = mutableListOf<User>()
        for(user: User in listOfUsers){
            if((user.firstName + " " + user.lastName).lowercase().contains(name.lowercase())){
                possibleResultsList.add(user)
            }
        }

        return possibleResultsList
    }

    private fun getUserData() = viewModelScope.launch {
        //Gets the requestedFriends, friends, and friendRequests lists from the database
        //and passes them to various functions for processing
        var friendIdList: HashMap<String, String>
        var requestedIdList: HashMap<String, String>
        var sentRequestIdList: HashMap<String, String>

        mainRepository.getMyUserData().addOnCompleteListener {
            if(it.isSuccessful){
                Log.d("firebase comms: ", "Retrieved friends from database")

                if(it.result!!.child("requestedFriends").getValue() != null){
                    sentRequestIdList = it.result!!.child("requestedFriends").getValue() as HashMap<String, String>
                    processSentFriendRequestIds(sentRequestIdList)
                }

                if(it.result!!.child("friends").getValue() != null){
                    friendIdList = it.result!!.child("friends").getValue() as HashMap<String, String>
                    processFriendIds(friendIdList)
                }

                if(it.result!!.child("friendRequests").getValue() != null){
                    requestedIdList = it.result!!.child("friendRequests").getValue() as HashMap<String, String>
                    processFriendRequestIds(requestedIdList)
                }
            }else {
                Log.d("firebase comms: ", "Unable to get friends ID list.")
            }
        }
    }

    private fun processFriendIds(friendIdList : HashMap<String, String>){
        //takes a list of friend id's and uses them to create a list of User objects
        for((key, value) in friendIdList){
            mainRepository.getUserData(value).addOnCompleteListener{
                if(it.isSuccessful){
                    val firstName = it.result!!.child("firstName").getValue().toString()
                    val lastName = it.result!!.child("lastName").getValue().toString()

                    userFriendsList.add(User(firstName, lastName, value, null))
                    friendsListMutableLiveData.postValue(userFriendsList)

                    for(sentRequest : User in sentFriendRequestsList){
                        if(sentRequest.id == value){
                            mainRepository.removeUserFromRequestedList(sentRequest.key!!)
                        }
                    }
                } else {
                    Log.d("firebase comms: ", "Unable to receive user data.")
                }
            }
        }
    }

    private fun processFriendRequestIds(requestedIdList : HashMap<String, String>){
        //takes a list of friend request id's and uses them to create a list of User objects
        for((key, value) in requestedIdList){
            mainRepository.getUserData(value).addOnCompleteListener{
                if(it.isSuccessful){
                    val firstName = it.result!!.child("firstName").getValue().toString()
                    val lastName = it.result!!.child("lastName").getValue().toString()

                    userRequestedList.add(User(firstName, lastName, value, key))
                    requestedListMutableLiveData.postValue(userRequestedList)
                } else {
                    Log.d("firebase comms: ", "Unable to receive user data.")
                }
            }
        }
    }

    private fun processSentFriendRequestIds(requestedIdList : HashMap<String, String>){
        //takes a list of requested friend id's and uses them to create a list of User objects
        for((key, value) in requestedIdList){
            mainRepository.getUserData(value).addOnCompleteListener{
                if(it.isSuccessful){
                    val firstName = it.result!!.child("firstName").getValue().toString()
                    val lastName = it.result!!.child("lastName").getValue().toString()

                    sentFriendRequestsList.add(User(firstName, lastName, value, key))

                } else {
                    Log.d("firebase comms: ", "Unable to receive user data.")
                }
            }
        }
    }

    fun getFriendsList() {
        //Get a list of the users friends from the database and store it
        //in a MutableLiveData object
        var friendIdList : HashMap<String, String>
        if(userFriendsList.isEmpty()) {
            runBlocking {
                launch(Dispatchers.Default){
                    mainRepository.getMyUserData().addOnCompleteListener {
                        if(it.isSuccessful){
                            Log.d("firebase comms: ", "Retrieved friends from database")
                            if(it.result!!.child("friends").getValue() != null){
                                friendIdList = it.result!!.child("friends").getValue() as HashMap<String, String>
                                processFriendIds(friendIdList)
                            }
                        }else {
                            Log.d("firebase comms: ", "Unable to get friends ID list.")
                        }
                    }
                    friendsListMutableLiveData.postValue(userFriendsList)
                }
            }
        } else {
            friendsListMutableLiveData.postValue(userFriendsList)
        }
    }

    fun getAllUsers() = runBlocking{
        //Get a list of all users on the platform as User objects and store them
        //in the listOfUsers variable
        if(listOfUsers.isEmpty()){
            launch(Dispatchers.Default) {
                Tasks.await(mainRepository.getAllUsers().addOnCompleteListener {
                    if (it.isSuccessful) {
                        Log.d("firebase comms", "Successfully received all users from database")

                        for (child: DataSnapshot in it.result!!.children) {
                            // For each row in the Users table
                            if(firebaseAuth.uid != child.child("userId").value.toString()){
                                // If the userId of the user doesn't match the currently signed
                                // in users Id, create a User object
                                var ignored = false
                                val user = User(
                                    child.child("firstName").value.toString(),
                                    child.child("lastName").value.toString(),
                                    child.child("userId").value.toString(),
                                    null
                                )

                                for(sentRequest: User in sentFriendRequestsList){
                                    //Check if the user is in the sentFriendRequest list,
                                    //as we don't want to show those users in the list
                                    if(sentRequest.id == user.id) ignored = true
                                }

                                if(!userFriendsList.contains(user) && !ignored){
                                    // If the user object isn't in the users list of friends,
                                    // add the the user to the listOfUsers variable
                                    listOfUsers.add(user)
                                }

                                friendsListMutableLiveData.postValue(listOfUsers)
                            }
                        }
                    } else {
                        Log.d("firebase comms", "Could not receive all users from database")
                    }
                })
            }
        } else {
            friendsListMutableLiveData.postValue(listOfUsers)
        }
    }

    fun sendFriendRequest(userId : String) {
        //Updates the friends list of the user who's Id matches userId, with the current
        //users ID
        mainRepository.sendFriendRequest(userId).addOnCompleteListener {
            if (it.isSuccessful) {
                Log.d("MainRepository", "Successfully sent friend request")
                mainRepository.updateRequestedList(userId).addOnCompleteListener { request ->
                    if (request.isSuccessful) {
                        Log.d("MainRepository", "Successfully updated users requested list")
                    } else {
                        Log.d("MainRepository", "Error: could not update users requested list")
                    }
                }
            } else {
                Log.d("MainRepository", "Error: could not send friend request")
            }
        }
    }

    fun acceptFriendRequest(userId: String, key: String) {
        //Handles necessary calls to the database to accept a friend request from another user
        mainRepository.addFriendToFriendsList(userId).addOnCompleteListener {
            if(it.isSuccessful){
                Log.d("firebase comms: ", "Successfully added new friend to friends list.")
            } else {
                Log.d("firebase comms: ", "Unable to add new friend to friends list.")
            }
        }

        mainRepository.removeFriendRequest(userId, key).addOnCompleteListener {
            if(it.isSuccessful){
                Log.d("firebase comms: ", "Successfully removed friend request.")
            } else {
                Log.d("firebase comms: ", "Unable to add remove friend request.")
            }
        }

        mainRepository.sendIdToNewFriend(userId).addOnCompleteListener {
            if(it.isSuccessful){
                Log.d("firebase comms: ", "Successfully sent user Id to new friend.")
            } else {
                Log.d("firebase comms: ", "Unable to send user Id to new friend.")
            }
        }
    }

    fun rejectFriendRequest(userId: String, key: String){
        //Removes the friend request from the users database entry
        mainRepository.removeFriendRequest(userId, key).addOnCompleteListener {
            if(it.isSuccessful){
                Log.d("firebase comms: ", "Successfully removed friend request.")

            } else {
                Log.d("firebase comms: ", "Unable to remove friend request.")
            }
        }
    }

    fun updateRequestedListAdapter(user: User, requestStatus: Int) : MutableList<User> {
        //If request status is 1, the user has accepted the friend request, so add
        //the friend to the friends list. Then remove the user from the requested list
        //and return the list
        if(requestStatus == 1){
            userFriendsList.add(user)
        }
        userRequestedList.remove(user)
        return userRequestedList
    }

    fun updateFriendsAdapter(user : User): MutableList<User> {
        //Removes a user from the list of users, this is called when the
        //user sends a friend request
        listOfUsers.remove(user)
        return listOfUsers
    }

    fun getFriendsListMutableLiveData() : MutableLiveData<MutableList<User>> {
        return friendsListMutableLiveData
    }

    fun getRequestedListMutableLiveData() : MutableLiveData<MutableList<User>> {
        return requestedListMutableLiveData
    }

    fun getUserFriendsList(): MutableList<User> {
        return userFriendsList
    }

    fun getUserRequestedList() :MutableList<User> {
        return userRequestedList
    }
}
