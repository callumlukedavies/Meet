package com.example.meet.view.event

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.meet.R
import com.example.meet.model.User
import com.example.meet.viewmodel.EventsViewModel

class EventInviteListAdapter(private var eventsViewModel: EventsViewModel, private var users : MutableList<User>) : RecyclerView.Adapter<EventInviteListAdapter.ViewHolder>() {
    private var invitedList: MutableList<String> = mutableListOf()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userItemNameTextView : TextView
        val userItemLinearLayout : LinearLayout
        val userItemButton : Button

        init {
            userItemNameTextView = itemView.findViewById(R.id.userItemName)
            userItemLinearLayout = itemView.findViewById(R.id.userItemLinearLayout)
            userItemButton = itemView.findViewById(R.id.userItemButton)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.invite_friends_item_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val name = users[position].firstName + " " + users[position].lastName
        val id = users[position].id
        var clicked = false

        holder.userItemNameTextView.setText(name)
        holder.userItemButton.setOnClickListener {
            clicked = !clicked
            if(clicked) {
                holder.userItemLinearLayout.setBackgroundColor((Color.parseColor("#5E8FED")))
                invitedList.add(id)
            } else {
                holder.userItemLinearLayout.setBackgroundColor((Color.parseColor("#DDDDDD")))
                invitedList.remove(id)
            }
        }
    }

    fun getInvitedList() : MutableList<String>{
        return invitedList
    }

    override fun getItemCount(): Int {
        return users.size
    }
}