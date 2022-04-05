package com.example.meet.view.viewevent

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.meet.R
import com.example.meet.model.Event
import com.example.meet.viewmodel.ViewEventViewModel
import com.example.meet.viewmodel.ViewEventViewModelFactory

class ViewEventActivity : AppCompatActivity(), ViewEventMainFragment.OnEventListener,
    ViewEventAttendingListFragment.OnEventListener,
    ViewEventInvitedListFragment.OnEventListener,
    ViewEventChatFragment.OnEventListener,
    ViewEventMapFragment.OnEventListener {

    private lateinit var viewEventViewModel: ViewEventViewModel
    private lateinit var titleTextView: TextView
    private lateinit var subtitleTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var CURRENT_FRAGMENT_TAG: String

    private var viewEventMainFragment: ViewEventMainFragment? = null
    private var viewEventAttendingListFragment: ViewEventAttendingListFragment? = null
    private var viewEventInvitedListFragment: ViewEventInvitedListFragment? = null
    private var viewEventChatFragment: ViewEventChatFragment? = null
    private var viewEventMapFragment: ViewEventMapFragment? = null

    private val MAIN_FRAGMENT_TAG = "ViewEventMainFragment"
    private val INVITED_LIST_TAG = "ViewEventInvitedListFragment"
    private val ATTENDING_LIST_TAG = "ViewEventAttendingListFragment"
    private val CHAT_FRAGMENT_TAG = "ViewEventChatFragment"
    private val MAP_FRAGMENT_TAG = "ViewEventMapFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_event)

        val actionBar = supportActionBar
        actionBar?.title = "View Event"
        actionBar?.setDisplayHomeAsUpEnabled(true)

        val eventId = intent.getStringExtra("eventId") as String
        val viewEventViewModelFactory = ViewEventViewModelFactory(application, eventId)
        viewEventViewModel = ViewModelProvider(this, viewEventViewModelFactory).get(ViewEventViewModel::class.java)

        titleTextView = findViewById(R.id.viewEventTitleTextView)
        subtitleTextView = findViewById(R.id.viewEventSubtitleTextView)
        descriptionTextView = findViewById(R.id.viewEventDescriptionTextView)

        viewEventViewModel.getEvent().observe(this, object : Observer<Event> {
            override fun onChanged(t: Event?) {
                titleTextView.setText(t!!.eventName)
                subtitleTextView.setText(t.eventDate + " at " + t.eventTime)
                descriptionTextView.setText(t.eventDescription)
            }
        })

        if(savedInstanceState != null){
            if(supportFragmentManager.findFragmentByTag(MAIN_FRAGMENT_TAG) != null)
                viewEventMainFragment = supportFragmentManager.findFragmentByTag(MAIN_FRAGMENT_TAG)
                        as ViewEventMainFragment
            else viewEventMainFragment = ViewEventMainFragment(viewEventViewModel)

            if(supportFragmentManager.findFragmentByTag(INVITED_LIST_TAG) != null)
                viewEventInvitedListFragment = supportFragmentManager.findFragmentByTag(INVITED_LIST_TAG)
                        as ViewEventInvitedListFragment
            else viewEventInvitedListFragment = ViewEventInvitedListFragment(viewEventViewModel)

            if(supportFragmentManager.findFragmentByTag(ATTENDING_LIST_TAG) != null)
                viewEventAttendingListFragment = supportFragmentManager.findFragmentByTag(ATTENDING_LIST_TAG)
                        as ViewEventAttendingListFragment
            else viewEventAttendingListFragment = ViewEventAttendingListFragment(viewEventViewModel)

        }else {
            viewEventMainFragment = ViewEventMainFragment(viewEventViewModel)

            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.add(R.id.fragmentLinearLayout, viewEventMainFragment!!, MAIN_FRAGMENT_TAG)
            fragmentTransaction.addToBackStack(MAIN_FRAGMENT_TAG)
            fragmentTransaction.commit()
            CURRENT_FRAGMENT_TAG = MAIN_FRAGMENT_TAG
        }
    }

    private fun hideFragment(ft: FragmentTransaction){
        when (CURRENT_FRAGMENT_TAG) {
            MAIN_FRAGMENT_TAG -> {
                ft.hide(viewEventMainFragment!!)
            }
            INVITED_LIST_TAG -> {
                ft.hide(viewEventInvitedListFragment!!)
            }
            ATTENDING_LIST_TAG -> {
                ft.hide(viewEventAttendingListFragment!!)
            }
            CHAT_FRAGMENT_TAG -> {
                ft.hide(viewEventChatFragment!!)
            }
            MAP_FRAGMENT_TAG -> {
                ft.hide(viewEventMapFragment!!)
            }
        }
    }

    override fun viewEventInvitedList() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        if(viewEventInvitedListFragment == null){
            viewEventInvitedListFragment = ViewEventInvitedListFragment(viewEventViewModel)
            hideFragment(fragmentTransaction)
            fragmentTransaction.add(R.id.fragmentLinearLayout, viewEventInvitedListFragment!!, INVITED_LIST_TAG)
            fragmentTransaction.addToBackStack(INVITED_LIST_TAG)
            fragmentTransaction.commit()
        } else {
            hideFragment(fragmentTransaction)
            fragmentTransaction.show(viewEventInvitedListFragment!!)
            fragmentTransaction.commit()
        }
        CURRENT_FRAGMENT_TAG = INVITED_LIST_TAG
    }

    override fun viewEventAttendingList() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        if(viewEventAttendingListFragment == null){
            viewEventAttendingListFragment = ViewEventAttendingListFragment(viewEventViewModel)
            hideFragment(fragmentTransaction)
            fragmentTransaction.add(R.id.fragmentLinearLayout, viewEventAttendingListFragment!!, ATTENDING_LIST_TAG)
            fragmentTransaction.addToBackStack(ATTENDING_LIST_TAG)
            fragmentTransaction.commit()
        } else {
            hideFragment(fragmentTransaction)
            fragmentTransaction.show(viewEventAttendingListFragment!!)
            fragmentTransaction.commit()
        }
        CURRENT_FRAGMENT_TAG = ATTENDING_LIST_TAG
    }

    override fun viewEventChat() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        if(viewEventChatFragment == null){
            viewEventChatFragment = ViewEventChatFragment(viewEventViewModel)
            hideFragment(fragmentTransaction)
            fragmentTransaction.add(R.id.fragmentLinearLayout, viewEventChatFragment!!, CHAT_FRAGMENT_TAG)
            fragmentTransaction.addToBackStack(CHAT_FRAGMENT_TAG)
            fragmentTransaction.commit()
        } else {
            hideFragment(fragmentTransaction)
            fragmentTransaction.show(viewEventChatFragment!!)
            fragmentTransaction.commit()
        }
        CURRENT_FRAGMENT_TAG = CHAT_FRAGMENT_TAG
    }

    override fun viewEventMap() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        if(viewEventMapFragment == null){
            viewEventMapFragment = ViewEventMapFragment(viewEventViewModel)
            hideFragment(fragmentTransaction)
            fragmentTransaction.add(R.id.fragmentLinearLayout, viewEventMapFragment!!, MAP_FRAGMENT_TAG)
            fragmentTransaction.addToBackStack(MAP_FRAGMENT_TAG)
            fragmentTransaction.commit()
        } else {
            hideFragment(fragmentTransaction)
            fragmentTransaction.show(viewEventMapFragment!!)
            fragmentTransaction.commit()
        }
        CURRENT_FRAGMENT_TAG = MAP_FRAGMENT_TAG
    }

    override fun acceptEventInvitation() {
        viewEventViewModel.acceptInvitation()
        val intent = Intent()
        intent.putExtra("decision", 1)
        val event = viewEventViewModel.getEvent().value!!.eventId
        intent.putExtra("eventId", event)
        setResult(Activity.RESULT_OK, intent)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        finish()
    }

    override fun rejectEventInvitation() {
        viewEventViewModel.rejectInvitation()
        val intent = Intent()
        intent.putExtra("decision", 1)
        val event = viewEventViewModel.getEvent().value!!.eventId
        intent.putExtra("eventId", event)
        setResult(Activity.RESULT_OK, intent)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        CURRENT_FRAGMENT_TAG = MAIN_FRAGMENT_TAG
        if(supportFragmentManager.backStackEntryCount == 0){
            finish()
        }
    }

    override fun returnToMainFragmentFromAttendingList() {
        returnToMainFragment()
    }

    override fun returnToMainFragmentFromInvitedList() {
        returnToMainFragment()
    }

    override fun returnToMainFragmentFromChat() {
        returnToMainFragment()
    }

    override fun returnToMainFragmentFromMap(){
        returnToMainFragment()
    }

    private fun returnToMainFragment(){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        if(viewEventMainFragment == null){
            viewEventMainFragment = ViewEventMainFragment(viewEventViewModel)
            hideFragment(fragmentTransaction)
            fragmentTransaction.add(R.id.fragmentLinearLayout, viewEventMainFragment!!, MAIN_FRAGMENT_TAG)
            fragmentTransaction.addToBackStack(MAIN_FRAGMENT_TAG)
            fragmentTransaction.commit()
        } else {
            hideFragment(fragmentTransaction)
            fragmentTransaction.show(viewEventMainFragment!!)
            fragmentTransaction.commit()
        }
        CURRENT_FRAGMENT_TAG = MAIN_FRAGMENT_TAG
    }

    override fun onDestroy() {
        super.onDestroy()
        viewEventViewModel.removeEventChatCallback()
    }
}