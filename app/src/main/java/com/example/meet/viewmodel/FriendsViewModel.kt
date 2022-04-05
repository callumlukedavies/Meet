package com.example.meet.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
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
    private var usersListAdapterMutableLiveData : MutableLiveData<MutableList<User>>

    //List of the users friends Id's
    private var userFriendsList : MutableList<User>
    //List of all users on the platform
    private var listOfUsers : MutableList<User>

    init {
        firebaseAuth = FirebaseAuth.getInstance()
        mainRepository = MainRepository()
        friendsListMutableLiveData = MutableLiveData()
        usersListAdapterMutableLiveData = MutableLiveData()
        userFriendsList = mutableListOf()
        listOfUsers = mutableListOf()
        getFriendsList()
    }

    fun searchFriendsList(name : String): MutableList<User> {
        val possibleResultsList = mutableListOf<User>()
        for(friend: User in userFriendsList){
            if((friend.firstName + " " + friend.lastName).contains(name)){
                possibleResultsList.add(friend)
            }
        }

        return possibleResultsList
    }

    fun searchUserList(name : String): MutableList<User> {
        val possibleResultsList = mutableListOf<User>()
        for(user: User in listOfUsers){
            if((user.firstName + " " + user.lastName).contains(name)){
                possibleResultsList.add(user)
            }
        }

        return possibleResultsList
    }

    fun getFriendsList () {
        //Get a list of the users friends from the database and store it
        //in a MutableLiveData object
        var friendIdList : MutableList<String>
        if(userFriendsList.isEmpty()) {
            runBlocking {
                launch(Dispatchers.Default){
                    mainRepository.getMyUserData().addOnCompleteListener {
                        if(it.isSuccessful){
                            Log.d("firebase comms: ", "Retrieved friends from database")
                            if(it.result!!.child("friends").getValue() != null){
                                friendIdList = it.result!!.child("friends").getValue() as MutableList<String>
                                processIds(friendIdList)
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

                            if(firebaseAuth.uid != child.child("userId").value.toString()){
                                val user = User(
                                    child.child("firstName").value.toString(),
                                    child.child("lastName").value.toString(),
                                    child.child("userId").value.toString()
                                )

                                if(!userFriendsList.contains(user)){
                                    listOfUsers.add(user)
                                }

                                println("Size is ${listOfUsers.size}")
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

    fun processIds(friendIdList : MutableList<String>){
        for(id : String in friendIdList){
            mainRepository.getUserData(id).addOnCompleteListener{
                if(it.isSuccessful){
                    val firstName = it.result!!.child("firstName").getValue().toString()
                    val lastName = it.result!!.child("lastName").getValue().toString()

                    userFriendsList.add(User(firstName, lastName, id))
                    friendsListMutableLiveData.postValue(userFriendsList)
                } else {
                    Log.d("firebase comms: ", "Unable to receive user data.")
                }
            }
        }
    }

    fun sendFriendRequest(userId : String){
        //Updates the friends list of the user who's Id matches userId, with the current
        //users ID
//        for(friend: User in userFriendsList){
//            if(friend.id == userId) return
//        }

        mainRepository.getFriendsOfUser(userId).addOnCompleteListener {
            if(it.isSuccessful){
                Log.d("firebase comms", "Successfully received friends of user.")
                var userFriends = mutableListOf<String>()
                if(it.result!!.getValue() != null){
                    userFriends = it.result!!.getValue() as MutableList<String>
                    userFriends.add(firebaseAuth.uid!!)
                } else {
                    userFriends.add(firebaseAuth.uid!!)
                }
                mainRepository.sendFriendRequest(userId, userFriends).addOnCompleteListener {
                    if(it.isSuccessful){
                        Log.d("firebase comms", "Successfully sent friend request to $userId.")
                        updateFriendsList(userId)
                    }
                }
            } else {
                Log.d("firebase comms", "Could not get friends of user.")
            }
        }
    }

    fun updateFriendsList(userId: String){
        //Updates the friends list of the user with the UserID passed as a variable
        mainRepository.getMyUserData().addOnCompleteListener {
            if(it.isSuccessful){
                Log.d("firebase comms", "Successfully received user data.")
                var friendIdList = mutableListOf<String>()
                if(it.result!!.child("friends").getValue() != null){
                    friendIdList = it.result!!.child("friends").getValue() as MutableList<String>
                    friendIdList.add(userId)
                } else {
                    friendIdList.add(userId)

                }
                mainRepository.updateFriendsList(friendIdList).addOnCompleteListener {
                    if(it.isSuccessful){
                        Log.d("firebase comms", "Successfully updated friends list for user.")
                    }
                    else {
                        Log.d("firebase comms", "Could not update friends list for user.")
                    }
                }
            }else {
                Log.d("firebase comms: ", "Unable to get friends ID list.")
            }
        }
    }

    fun getFriendsListMutableLiveData() : MutableLiveData<MutableList<User>> {
        return friendsListMutableLiveData
    }

    fun getUserListAdapterMutableLiveData() : MutableLiveData<MutableList<User>> {
        return this.usersListAdapterMutableLiveData
    }

    fun isUserListPopulated() : Boolean {
        return listOfUsers.isNotEmpty()
    }

    fun isFriendsListPopulated() : Boolean {
        return userFriendsList.isNotEmpty()
    }

    fun getUserFriendsList(): MutableList<User> {
        return userFriendsList
    }

    fun updateFriendsAdapter(user : User): MutableList<User> {
        userFriendsList.add(user)
        listOfUsers.remove(user)
        return listOfUsers
    }

//    fun getUserAdapter(): UserListAdapter {
//        getAllUsers()
//        usersListAdapter = UserListAdapter(this, listOfUsers)
//        return usersListAdapter
//    }
}
