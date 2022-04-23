package com.example.meet.view.calendar

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.meet.R
import com.example.meet.view.viewevent.ViewEventActivity
import com.example.meet.viewmodel.*

/**
 * A simple [Fragment] subclass.
 * Use the [CalendarFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CalendarFragment : Fragment(), CalendarListAdapter.OnEventListener {

    private lateinit var calendarViewModel: CalendarViewModel
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private lateinit var calendarListAdapter: CalendarListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                val decision = data!!.getIntExtra("decision", 0)
                val eventId = data.getStringExtra("eventId") as String
                if(decision == 1) calendarListAdapter.setData(calendarViewModel.removeEventFromList(eventId))
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calendar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val calendarViewModelFactory = CalendarViewModelFactory(requireActivity().application)
        calendarViewModel = ViewModelProvider(this, calendarViewModelFactory).get(CalendarViewModel::class.java)
        val linearLayoutManager = LinearLayoutManager(context)
        calendarListAdapter = CalendarListAdapter(mutableListOf(), this.requireContext(),this)
        val calendarRecyclerView = view.findViewById<RecyclerView>(R.id.calendarRecyclerView)
        val noEventsTextView = view.findViewById<TextView>(R.id.noEventsTextView)

        calendarRecyclerView.layoutManager = linearLayoutManager
        calendarRecyclerView.adapter = calendarListAdapter

        calendarViewModel.getEventsMutableLiveData().observe(viewLifecycleOwner
        ) {
            calendarListAdapter.setData(it)
            if(it.size > 0) noEventsTextView.visibility = View.GONE
        }
    }

    private fun updateCalendar(){
        if(this::calendarViewModel.isInitialized) calendarViewModel.getEvents()
    }

    override fun callback(eventId: String) {
        val intent = Intent(activity, ViewEventActivity::class.java)
        intent.putExtra("eventId", eventId)
        resultLauncher.launch(intent)
    }

    override fun onResume() {
        super.onResume()
        Log.d("CalendarFragment", "onResume")
        updateCalendar()
    }

    override fun onPause() {
        super.onPause()
        Log.d("CalendarFragment", "onPause")
    }
}