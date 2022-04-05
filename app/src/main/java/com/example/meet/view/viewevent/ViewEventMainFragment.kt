package com.example.meet.view.viewevent

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.example.meet.R
import com.example.meet.viewmodel.ViewEventViewModel


/**
 * A simple [Fragment] subclass.
 * Use the [ViewEventMainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ViewEventMainFragment(private val viewEventViewModel: ViewEventViewModel) : Fragment() {

    private lateinit var onEventListener: OnEventListener

    interface OnEventListener {
        fun viewEventInvitedList()
        fun viewEventAttendingList()
        fun viewEventChat()
        fun viewEventMap()
        fun acceptEventInvitation()
        fun rejectEventInvitation()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_event_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var invitedListButton = view.findViewById<Button>(R.id.viewEventInvitedListButton)
        var attendingListButton = view.findViewById<Button>(R.id.viewEventAttendingListButton)
        val acceptInvitationButton = view.findViewById<Button>(R.id.viewEventAcceptButton)
        val rejectInvitationButton = view.findViewById<Button>(R.id.viewEventRejectButton)
        val viewChatButton = view.findViewById<Button>(R.id.viewEventChatButton)
        val viewMapButton = view.findViewById<Button>(R.id.viewEventMapButton)
        val chatButtonLayout = view.findViewById<LinearLayout>(R.id.chatButtonLayout)
        val respondButtonLayout = view.findViewById<LinearLayout>(R.id.respondButtonLayout)

        invitedListButton.setOnClickListener {
            onEventListener.viewEventInvitedList()
        }

        attendingListButton.setOnClickListener {
            onEventListener.viewEventAttendingList()
        }

        viewChatButton.setOnClickListener {
            onEventListener.viewEventChat()
        }

        viewMapButton.setOnClickListener {
            onEventListener.viewEventMap()
        }

        acceptInvitationButton.setOnClickListener{
            onEventListener.acceptEventInvitation()
        }

        rejectInvitationButton.setOnClickListener {
            onEventListener.rejectEventInvitation()
        }

        viewEventViewModel.getIsUserAttendingMutableLiveData().observe(viewLifecycleOwner) {
            if(it) {
                chatButtonLayout.visibility = View.VISIBLE
                respondButtonLayout.visibility = View.GONE
            } else {
                chatButtonLayout.visibility = View.GONE
                respondButtonLayout.visibility = View.VISIBLE
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is OnEventListener){
            onEventListener = context
        } else {
            throw ClassCastException(context.toString()
                    + "has not implemented interface ViewEventMainFragment.OnEventListener")
        }
    }

}