package com.example.meet.view.event

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.meet.R
import com.example.meet.viewmodel.EventsViewModel

class EventsThreeFragment(private val eventsViewModel: EventsViewModel) : Fragment() {

    private lateinit var onEventListener: OnEventListener
    private lateinit var recyclerView: RecyclerView
    private lateinit var continueButton: Button
    private lateinit var cancelButton: Button
    private lateinit var invitedTextView: TextView

    interface OnEventListener {
        fun eventsThreeFragmentCompleted(invitedList: MutableList<String>)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event_three, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val inviteListAdapter = EventInviteListAdapter(eventsViewModel, eventsViewModel.getUserFriendsList())
        val linearLayoutManager = LinearLayoutManager(requireContext())
        recyclerView = view.findViewById(R.id.eventThreeFragmentRecyclerView)
        continueButton = view.findViewById(R.id.eventThreeFragmentContinueButton)
        cancelButton = view.findViewById<Button>(R.id.eventThreeFragmentCancelButton)

        //recyclerView setup
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = inviteListAdapter

        //cancelButton setup
        cancelButton.setOnClickListener {
            requireActivity().finish()
        }

        //continueButton setup
        continueButton.setOnClickListener {
            onEventListener.eventsThreeFragmentCompleted(inviteListAdapter.getInvitedList())
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is OnEventListener){
            onEventListener = context
        } else {
            throw ClassCastException(context.toString()
                    + "has not implemented interface EventTwoFragment.OnEventListener")
        }
    }
}