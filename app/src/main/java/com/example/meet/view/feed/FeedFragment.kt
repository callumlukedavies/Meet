package com.example.meet.view.feed

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.meet.R
import com.example.meet.view.event.EventsActivity
import com.example.meet.view.viewevent.ViewEventActivity
import com.example.meet.viewmodel.FeedViewModel
import com.example.meet.viewmodel.FeedViewModelFactory

/**
 * A simple [Fragment] subclass.
 * Use the [FeedFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FeedFragment : Fragment(), InvitationListAdapter.OnEventListener {

    private lateinit var feedViewModel: FeedViewModel
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private lateinit var invitationListAdapter: InvitationListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                val decision = data!!.getIntExtra("decision", 0)
                val eventId = data.getStringExtra("eventId") as String
                if(decision == 1) invitationListAdapter.setData(feedViewModel.removeEventFromList(eventId))
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val feedViewModelFactory = FeedViewModelFactory(requireActivity().application)
        feedViewModel = ViewModelProvider(this, feedViewModelFactory).get(FeedViewModel::class.java)
        invitationListAdapter = InvitationListAdapter(mutableListOf(), this.requireContext(), this)
        val feedCreateEventButton = view.findViewById<Button>(R.id.feedCreateEventButton)
        val feedInvitationRecyclerView = view.findViewById<RecyclerView>(R.id.feedInvitationRecyclerView)
        val linearLayoutManager = LinearLayoutManager(context)

        feedInvitationRecyclerView.layoutManager = linearLayoutManager
        feedInvitationRecyclerView.adapter = invitationListAdapter
        feedViewModel.getInvitationsMutableLiveData().observe(viewLifecycleOwner
        ) {
            invitationListAdapter.setData(it)
            println("Data changed!")
        }

        feedCreateEventButton.setOnClickListener {
            val intent = Intent(activity, EventsActivity::class.java)
            startActivity(intent)
        }
    }

    override fun callback(eventId: String) {
        val intent = Intent(activity, ViewEventActivity::class.java)
        intent.putExtra("eventId", eventId)
        resultLauncher.launch(intent)
    }
}