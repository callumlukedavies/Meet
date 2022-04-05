package com.example.meet.view.event

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.meet.R

class EventFourFragment : Fragment() {
    private lateinit var descriptionEditText: EditText
    private lateinit var onEventListener: OnEventListener

    interface OnEventListener {
        fun eventsFourFragmentCompleted(eventDescription: String)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event_four, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        descriptionEditText = view.findViewById(R.id.eventDescriptionEditText)
        val confirmButton = view.findViewById<Button>(R.id.eventFourFragmentConfirmButton)
        val cancelButton = view.findViewById<Button>(R.id.eventFourFragmentCancelButton)

        cancelButton.setOnClickListener {
            requireActivity().finish()
        }

        confirmButton.setOnClickListener {
            var description = descriptionEditText.text.toString()
            if(description.isEmpty()) description = ""
            onEventListener.eventsFourFragmentCompleted(description)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is OnEventListener){
            onEventListener = context
        } else {
            throw ClassCastException(context.toString()
                    + "has not implemented interface EventThreeFragment.OnEventListener")
        }
    }

}