package com.example.meet.view.friends

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

class UserListAdapter(private var friendsViewModel : FriendsViewModel, private var users : MutableList<User>) : RecyclerView.Adapter<UserListAdapter.ViewHolder>() {
    private var friendsToggled = true

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        val userItemNameTextView : TextView
        val userItemLinearLayout : LinearLayout
        val userItemButton : Button

        init {
            userItemNameTextView = view.findViewById(R.id.userItemName)
            userItemLinearLayout = view.findViewById(R.id.userItemLinearLayout)
            userItemButton = view.findViewById(R.id.userItemButton)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val name = users[position].firstName + " " + users[position].lastName
        val id = users[position].id

        holder.userItemNameTextView.setText(name)
        if(!friendsToggled)
        holder.userItemButton.setOnClickListener {
            println(users[position].firstName + ", " + users[position].id)
            friendsViewModel.updateFriendsAdapter(users[position])
            friendsViewModel.sendFriendRequest(id)
        }




    }

    override fun getItemCount(): Int {
        return users.size
    }

    fun updateUsersList(updatedList : MutableList<User>, isFriendsToggled: Boolean){
        users = updatedList
    }
}