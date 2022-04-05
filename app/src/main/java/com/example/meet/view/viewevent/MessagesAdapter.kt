package com.example.meet.view.viewevent

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.meet.R
import com.example.meet.model.Message

class MessagesAdapter(private var messagesList: MutableList<Message>, private val userId: String): RecyclerView.Adapter<MessagesAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var messageLayout = itemView.findViewById<LinearLayout>(R.id.messageLayout)
        var nameTextView = itemView.findViewById<TextView>(R.id.nameTextView)
        var timeTextView = itemView.findViewById<TextView>(R.id.timeTextView)
        var messageTextView = itemView.findViewById<TextView>(R.id.messageTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.message_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.nameTextView.setText(messagesList[position].userName)
        holder.timeTextView.setText(messagesList[position].time)
        holder.messageTextView.setText(messagesList[position].message)
        Log.d("MessagesAdapter", "ID: ${messagesList[position].userId}, userId: $userId")
        val params = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
        if(messagesList[position].userId == userId){
            params.addRule(RelativeLayout.ALIGN_PARENT_END)
            params.topMargin = 20
            holder.messageLayout.layoutParams = params
            holder.messageLayout.setBackgroundResource(R.drawable.user_message_item_background)
        }else {
            params.topMargin = 20
            holder.messageLayout.layoutParams = params
        }
    }

    override fun getItemCount(): Int {
        return messagesList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newList: MutableList<Message>){
        messagesList = newList
        notifyDataSetChanged()
    }

    fun appendData(message: Message) {
        messagesList.add(message)
        notifyItemInserted(messagesList.size)
    }
}