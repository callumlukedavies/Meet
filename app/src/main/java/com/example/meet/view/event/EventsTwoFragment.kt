package com.example.meet.view.event

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TimePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.meet.R
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode


/**
 * A simple [Fragment] subclass.
 * Use the [EventsTwoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EventsTwoFragment : Fragment(){

    private lateinit var eventTimePicker: TimePicker
    private lateinit var eventLocationEditText: EditText
    private lateinit var onEventListener: OnEventListener

    interface OnEventListener {
        fun eventsTwoFragmentCompleted(eventTime: String, eventPlaceName: String, eventPlaceId: String)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event_two, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val continueButton = view.findViewById<Button>(R.id.eventTwoFragmentContinueButton)
        val cancelButton = view.findViewById<Button>(R.id.eventTwoFragmentCancelButton)
        eventTimePicker = view.findViewById(R.id.eventTimePicker)
        var placeName = ""
        var placeId = ""

        // Initialize the SDK
        Places.initialize(requireActivity().applicationContext, resources.getString(R.string.google_maps_key))

        // Create a new PlacesClient instance
        val placesClient = Places.createClient(requireContext())

        // Initialize the AutocompleteSupportFragment.
        val autocompleteFragment =
            childFragmentManager.findFragmentById(R.id.autoCompleteFragment)
                    as AutocompleteSupportFragment

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME))

        autocompleteFragment.setActivityMode(AutocompleteActivityMode.FULLSCREEN)

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onError(p0: Status) {
                Log.i(ContentValues.TAG, "An error occurred: $p0")
            }

            override fun onPlaceSelected(place: Place) {
                Log.i(ContentValues.TAG, "Place: name: ${place.name}, id: ${place.id}")
                placeName = place.name
                placeId = place.id
            }
        })

        cancelButton.setOnClickListener {
            requireActivity().finish()
        }

        continueButton.setOnClickListener {
            var eventHour = eventTimePicker.hour.toString()
            var eventMinute = eventTimePicker.minute.toString()
            //convert hour into appropriate string
            if(eventHour == "0") eventHour += "0"
            else if(eventHour.length == 1) eventHour = "0$eventHour"
            //convert minute into appropriate string
            if(eventMinute == "0") eventMinute += "0"
            else if(eventMinute.length == 1) eventMinute = "0$eventMinute"

            val eventTime = "$eventHour:$eventMinute"

            if(eventTime.isNotEmpty() && placeName.isNotEmpty() && placeId.isNotEmpty()){
                onEventListener.eventsTwoFragmentCompleted(eventTime, placeName, placeId)
            } else {
                Toast.makeText(context, "Please choose a location and time", Toast.LENGTH_SHORT).show()
            }
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        val eventLocation = eventLocationEditText.text.toString()
        outState.putString("eventLocation", eventLocation)
    }
}