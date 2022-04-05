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

class FriendListAdapter(private var users: MutableList<User>, private var friendsViewModel: FriendsViewModel) : RecyclerView.Adapter<FriendListAdapter.ViewHolder>() {
    private var isFriendsToggled = true

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        val friendItemNameTextView : TextView
        val friendItemLinearLayout : LinearLayout
        val userItemButton : Button

        init {
            friendItemNameTextView = view.findViewById(R.id.friendItemName)
            friendItemLinearLayout = view.findViewById(R.id.friendItemLinearLayout)
            userItemButton = view.findViewById(R.id.friendItemButton)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.friends_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val name = "${users[position].firstName} ${users[position].lastName}"

        val id = users[position].id
        holder.friendItemNameTextView.setText(name)

        if(!isFriendsToggled){
            holder.userItemButton.setOnClickListener {
                println(users[position].firstName + ", " + users[position].id)
                setData(friendsViewModel.updateFriendsAdapter(users[position]), false)
                friendsViewModel.sendFriendRequest(id)
            }
            holder.userItemButton.visibility = View.VISIBLE
        } else {
            holder.userItemButton.visibility = View.GONE
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