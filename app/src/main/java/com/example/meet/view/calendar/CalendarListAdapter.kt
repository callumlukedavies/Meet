package com.example.meet.view.calendar

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.meet.R
import com.example.meet.model.Event
import com.example.meet.view.feed.InvitationListAdapter

class CalendarListAdapter(private var eventList: MutableList<Event>,
                          private var context: Context,
                          listener: OnEventListener)
    : RecyclerView.Adapter<InvitationListAdapter.ViewHolder>() {

    interface OnEventListener{
        fun callback(eventId: String)
    }

    private var onEventListener: OnEventListener = listener

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        var eventTitle: TextView = view.findViewById(R.id.eventTitle)
        var eventDate: TextView = view.findViewById(R.id.eventDate)
        var eventButton: Button = view.findViewById(R.id.viewEventButton)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): InvitationListAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.event_item, parent, false)
        return InvitationListAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: InvitationListAdapter.ViewHolder, position: Int) {
        val title = eventList[position].eventName + " at " + eventList[position].eventPlaceName
        val date = eventList[position].eventDate + ", " + eventList[position].eventTime

        holder.eventTitle.text = title
        holder.eventDate.text = date
        holder.eventButton.setOnClickListener {
            onEventListener.callback(eventList[position].eventId)
        }
    }

    override fun getItemCount(): Int {
        return eventList.size
    }

    fun setData(newInvitationsList: MutableList<Event>){
        eventList = newInvitationsList
        notifyDataSetChanged()
    }
}