package com.example.meet.view.viewevent

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.meet.R
import com.example.meet.model.User

class AttendingListAdapter(private var attendingList: MutableList<User>): RecyclerView.Adapter<AttendingListAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemTextView = itemView.findViewById<TextView>(R.id.userItemName)
        var imageView = itemView.findViewById<ImageView>(R.id.userItemImageView)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.invited_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val name = "${attendingList[position].firstName} ${attendingList[position].lastName}"
        holder.itemTextView.setText(name)
    }

    override fun getItemCount(): Int {
        return attendingList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newList: MutableList<User>){
        attendingList = newList
        notifyDataSetChanged()
    }
}