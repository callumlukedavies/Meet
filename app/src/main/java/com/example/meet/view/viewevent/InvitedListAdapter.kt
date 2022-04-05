package com.example.meet.view.viewevent

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.meet.R
import com.example.meet.model.User

class InvitedListAdapter(private var invitedList: MutableList<User>) : RecyclerView.Adapter<InvitedListAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var userItemName = itemView.findViewById<TextView>(R.id.userItemName)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.invited_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val name = "${invitedList[position].firstName} ${invitedList[position].lastName}"
        println(name)
        holder.userItemName.setText(name)
    }

    override fun getItemCount(): Int {
        return invitedList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newList: MutableList<User>){
        invitedList = newList
        notifyDataSetChanged()
    }
}