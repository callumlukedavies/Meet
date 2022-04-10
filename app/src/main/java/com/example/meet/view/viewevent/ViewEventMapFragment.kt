package com.example.meet.view.viewevent

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.meet.R
import com.example.meet.viewmodel.ViewEventViewModel


class ViewEventMapFragment(private var viewEventViewModel: ViewEventViewModel): Fragment(){

    private lateinit var onEventListener: OnEventListener
    private lateinit var mapsFragment: MapsFragment

    interface OnEventListener {
        fun returnToMainFragmentFromMap()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("ViewEventMapFragment", "onCreate")
        mapsFragment = MapsFragment(viewEventViewModel.getEventPlaceId())
        childFragmentManager.beginTransaction().add(R.id.viewEventMapLinearLayout, mapsFragment).commit()
        Log.d("ViewEventMapFragment", "finishing in onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_event_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fragmentImageView = view.findViewById<ImageView>(R.id.viewEventFragmentImageView)
        fragmentImageView.setOnClickListener{
            onEventListener.returnToMainFragmentFromMap()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is OnEventListener){
            onEventListener = context
        } else {
            throw ClassCastException(context.toString()
                    + "has not implemented interface ViewEventMapFragment.OnEventListener")
        }
    }
}