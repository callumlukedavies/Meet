package com.example.meet.view.friends

import android.app.Application
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.meet.R
import com.example.meet.model.User
import com.example.meet.viewmodel.FriendsViewModel
import com.example.meet.viewmodel.FriendsViewModelFactory

/**
 * A simple [Fragment] subclass.
 * Use the [FriendsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FriendsFragment : Fragment() {
    private lateinit var application: Application
    private lateinit var friendsViewModel: FriendsViewModel
    private lateinit var friendsFragmentEditText: EditText
    private lateinit var friendsFragmentTitleTextView: TextView
    private lateinit var friendsFragmentFriendsButton: Button
    private lateinit var friendsFragmentPeopleButton: Button
    private lateinit var friendsListAdapter : FriendListAdapter
    private lateinit var userListAdapter: UserListAdapter

    private var isFriendsToggled = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_friends, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val linearLayoutManager = LinearLayoutManager(context)
        val friendsFragmentRecyclerView = view.findViewById<RecyclerView>(R.id.friendsFragmentRecyclerView)
        val friendsFragmentSearchButton = view.findViewById<Button>(R.id.friendsFragmentSearchButton)
        friendsFragmentPeopleButton = view.findViewById(R.id.friendsFragmentPeopleButton)
        friendsFragmentFriendsButton = view.findViewById(R.id.friendsFragmentFriendsButton)
        friendsFragmentEditText = view.findViewById(R.id.friendsFragmentEditText)
        friendsFragmentTitleTextView = view.findViewById(R.id.friendsFragmentTitleTextView)
        friendsFragmentRecyclerView.layoutManager = linearLayoutManager

        try{
            application = requireActivity().application
            val friendsViewModelFactory = FriendsViewModelFactory(application)
            friendsViewModel = ViewModelProvider(this, friendsViewModelFactory).get(FriendsViewModel::class.java)
            friendsListAdapter = FriendListAdapter(friendsViewModel.getUserFriendsList(), friendsViewModel)
            friendsFragmentRecyclerView.adapter = friendsListAdapter
        } catch (e: IllegalStateException) {
            Log.d("FriendsFragment", "IllegalStateException, can't obtain application")
        }

        if(savedInstanceState != null){
            isFriendsToggled = savedInstanceState.getBoolean("isFriendsToggled", true)
            if(!isFriendsToggled){
                isFriendsToggled = true
                friendsFragmentPeopleButton.setBackgroundColor(Color.parseColor("#CCCCCC"))
                friendsFragmentFriendsButton.setBackgroundColor(Color.parseColor("#DDDDDD"))
            }
        } else {
            isFriendsToggled = true
        }

        friendsViewModel.getFriendsListMutableLiveData().observe(viewLifecycleOwner, object :
            Observer<MutableList<User>> {
            override fun onChanged(userList : MutableList<User>) {
                Log.d("FriendsFragment", "updating friendListAdapter")
                friendsListAdapter.setData(userList, isFriendsToggled)
                val titleText = "${friendsListAdapter.itemCount} users"
                friendsFragmentTitleTextView.setText(titleText)
            }
        })

        friendsFragmentPeopleButton.setOnClickListener {
            if(isFriendsToggled){
                friendsFragmentEditText.setText("")
                toggleView()
                friendsViewModel.getAllUsers()
//                val titleText = "${friendsListAdapter.itemCount} users"
//                friendsFragmentTitleTextView.setText(titleText)
            }
        }

        friendsFragmentFriendsButton.setOnClickListener {
            if(!isFriendsToggled){
                friendsFragmentEditText.setText("")
                toggleView()
                friendsViewModel.getFriendsList()
//                val titleText = "${friendsListAdapter.itemCount} friends"
//                friendsFragmentTitleTextView.setText(titleText)
            }
        }

        friendsFragmentSearchButton.setOnClickListener {
            val searchText = friendsFragmentEditText.text.toString()
            if(isFriendsToggled){
                friendsListAdapter.setData(friendsViewModel.searchFriendsList(searchText), isFriendsToggled)
                val titleText = if (friendsListAdapter.itemCount == 1) "1 result"
                                else "${friendsListAdapter.itemCount} results"
                friendsFragmentTitleTextView.setText(titleText)

            }else {
                friendsListAdapter.setData(friendsViewModel.searchUserList(searchText), isFriendsToggled)
                val titleText = if (friendsListAdapter.itemCount == 1) "1 result"
                                else "${friendsListAdapter.itemCount} results"
                friendsFragmentTitleTextView.setText(titleText)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putBoolean("isFriendsToggled", isFriendsToggled)
        outState.putString("friendsFragmentEditTextText", friendsFragmentEditText.text.toString())
    }

    private fun toggleView(){
        if(isFriendsToggled){
            isFriendsToggled = false
            friendsFragmentPeopleButton.setBackgroundColor(Color.parseColor("#CCCCCC"))
            friendsFragmentFriendsButton.setBackgroundColor(Color.parseColor("#DDDDDD"))
        }else {
            isFriendsToggled = true
            friendsFragmentPeopleButton.setBackgroundColor(Color.parseColor("#DDDDDD"))
            friendsFragmentFriendsButton.setBackgroundColor(Color.parseColor("#CCCCCC"))
        }
    }
}