package com.example.meet.view.event

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.meet.R
import com.example.meet.viewmodel.EventsViewModel
import java.util.*

class EventOneFragment : Fragment() {
    private lateinit var eventNameEditText: EditText
    private lateinit var eventDatePicker: DatePicker

    private lateinit var onEventListener: OnEventListener


    interface OnEventListener {
        fun eventsOneFragmentCompleted(eventName: String, eventDate: String)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event_one, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cancelButton = view.findViewById<Button>(R.id.eventOneFragmentCancelButton)
        val continueButton = view.findViewById<Button>(R.id.eventOneFragmentContinueButton)

        eventNameEditText = view.findViewById(R.id.eventNameEditText)
        eventDatePicker = view.findViewById(R.id.eventDatePicker)

        eventDatePicker.minDate = Calendar.getInstance().timeInMillis

        if(savedInstanceState != null){
            val eventName = savedInstanceState.getString("eventName", "")
            val eventDate = savedInstanceState.getString("eventDate", "")

            eventNameEditText.setText(eventName)
        }

        cancelButton.setOnClickListener {
            requireActivity().finish()
        }

        continueButton.setOnClickListener {
            val eventName = eventNameEditText.text.toString()

            if(EventsViewModel.validateEventName(eventName)){
                val eventDate = eventDatePicker.dayOfMonth.toString() +
                        "/" + (eventDatePicker.month + 1).toString() + "/" +
                        eventDatePicker.year.toString()
                println("Date is $eventDate")
                onEventListener.eventsOneFragmentCompleted(eventName, eventDate)
            } else {
                Toast.makeText(context, "Please choose a name for the event", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
//        val eventName = eventNameEditText.text.toString()
//        val eventDate = eventDateEditText.text.toString()
//
//
//        outState.putString("eventName", eventName)
//        outState.putString("eventDate", eventDate)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is OnEventListener){
            onEventListener = context
        } else {
            throw ClassCastException(context.toString()
                    + "has not implemented interface EventOneFragment.OnEventListener")
        }
    }
}