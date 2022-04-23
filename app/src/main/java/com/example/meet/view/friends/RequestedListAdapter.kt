package com.example.meet.view.friends

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.meet.R
import com.example.meet.model.User
import com.example.meet.viewmodel.FriendsViewModel

class RequestedListAdapter(private var friendsViewModel : FriendsViewModel, private var users : MutableList<User>) : RecyclerView.Adapter<RequestedListAdapter.ViewHolder>() {
    private var isFriendsToggled = true

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        val userItemNameTextView : TextView
        val userItemLinearLayout : LinearLayout
        val acceptItemButton : Button
        val rejectItemButton : Button

        init {
            userItemNameTextView = view.findViewById(R.id.userItemName)
            userItemLinearLayout = view.findViewById(R.id.userItemLinearLayout)
            acceptItemButton = view.findViewById(R.id.acceptItemButton)
            rejectItemButton = view.findViewById(R.id.rejectItemButton)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.requested_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val name = users[position].firstName + " " + users[position].lastName
        val id = users[position].id

        holder.userItemNameTextView.setText(name)

        holder.acceptItemButton.setOnClickListener {
            println(users[position].firstName + ", " + users[position].id)
            users[position].key?.let { key ->
                friendsViewModel.acceptFriendRequest(users[position].id, key)
                setData(friendsViewModel.updateRequestedListAdapter(users[position], 1), false)
            }
        }

        holder.rejectItemButton.setOnClickListener {
            users[position].key?.let { key ->
                friendsViewModel.rejectFriendRequest(users[position].id, key)
                setData(friendsViewModel.updateRequestedListAdapter(users[position], 0), false)
            }
        }
    }

    override fun getItemCount(): Int {
        return users.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newUsers: MutableList<User>, friendsToggled: Boolean){
        users = newUsers
        notifyDataSetChanged()
        isFriendsToggled = friendsToggled
    }
}