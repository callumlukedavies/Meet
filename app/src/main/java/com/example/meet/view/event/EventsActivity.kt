package com.example.meet.view.event

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.meet.R
import com.example.meet.viewmodel.EventsViewModel
import com.example.meet.viewmodel.EventsViewModelFactory

class EventsActivity : AppCompatActivity(), EventOneFragment.OnEventListener,
    EventsTwoFragment.OnEventListener, EventsThreeFragment.OnEventListener,
    EventFourFragment.OnEventListener {
    private lateinit var eventsViewModel: EventsViewModel
    private var eventsOneFragment: EventOneFragment? = null
    private var eventsThreeFragment: EventsThreeFragment? = null
    private var eventsTwoFragment: EventsTwoFragment? = null
    private var eventsFourFragment: EventFourFragment? = null
    private val FRAGMENT_ONE_TAG = "EventsOneFragment"
    private val FRAGMENT_TWO_TAG = "EventsTwoFragment"
    private val FRAGMENT_THREE_TAG = "EventsThreeFragment"
    private val FRAGMENT_FOUR_TAG = "EventsFourFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_events)
        val eventsViewModelFactory = EventsViewModelFactory(application)
        eventsViewModel = ViewModelProvider(this, eventsViewModelFactory).get(EventsViewModel::class.java)

        if(savedInstanceState != null){
            if(supportFragmentManager.findFragmentByTag(FRAGMENT_ONE_TAG) != null)
                eventsOneFragment = supportFragmentManager.findFragmentByTag(FRAGMENT_ONE_TAG)
                        as EventOneFragment
            else eventsOneFragment = EventOneFragment()

            eventsTwoFragment = if(supportFragmentManager.findFragmentByTag(FRAGMENT_TWO_TAG) != null)
                supportFragmentManager.findFragmentByTag(FRAGMENT_TWO_TAG) as EventsTwoFragment
            else EventsTwoFragment()

            eventsThreeFragment = if(supportFragmentManager.findFragmentByTag(FRAGMENT_THREE_TAG) != null)
                supportFragmentManager.findFragmentByTag(FRAGMENT_THREE_TAG)
                        as EventsThreeFragment
            else EventsThreeFragment(eventsViewModel)

            eventsFourFragment = if(supportFragmentManager.findFragmentByTag(FRAGMENT_FOUR_TAG) != null)
                supportFragmentManager.findFragmentByTag(FRAGMENT_FOUR_TAG)
                        as EventFourFragment
            else EventFourFragment()

        }else {
            eventsOneFragment = EventOneFragment()
            eventsTwoFragment = EventsTwoFragment()
            eventsThreeFragment = EventsThreeFragment(eventsViewModel)
            eventsFourFragment = EventFourFragment()

            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.add(R.id.eventFragmentLinearLayout, eventsOneFragment!!, FRAGMENT_ONE_TAG)
            fragmentTransaction.addToBackStack(FRAGMENT_ONE_TAG)
            fragmentTransaction.commit()
        }
    }

    private fun changeFragment(backStackCount : Int) {

        when(backStackCount){
            1 -> {
                val fragmentTransaction = supportFragmentManager.beginTransaction()
                fragmentTransaction.hide(eventsOneFragment!!)
                fragmentTransaction.add(R.id.eventFragmentLinearLayout, eventsTwoFragment!!, FRAGMENT_TWO_TAG)
                fragmentTransaction.addToBackStack(FRAGMENT_TWO_TAG)
                fragmentTransaction.commit()
            }

            2 -> {
                val fragmentTransaction = supportFragmentManager.beginTransaction()
                fragmentTransaction.hide(eventsTwoFragment!!)
                fragmentTransaction.add(R.id.eventFragmentLinearLayout, eventsThreeFragment!!, FRAGMENT_THREE_TAG)
                fragmentTransaction.addToBackStack(FRAGMENT_THREE_TAG)
                fragmentTransaction.commit()
            }

            3 -> {
                val fragmentTransaction = supportFragmentManager.beginTransaction()
                fragmentTransaction.hide(eventsThreeFragment!!)
                fragmentTransaction.add(R.id.eventFragmentLinearLayout, eventsFourFragment!!, FRAGMENT_FOUR_TAG)
                fragmentTransaction.addToBackStack(FRAGMENT_FOUR_TAG)
                fragmentTransaction.commit()
            }
        }
    }

    override fun eventsOneFragmentCompleted(
        eventName: String, eventDate: String
    ) {
        eventsViewModel.setEventName(eventName)
        eventsViewModel.setEventDate(eventDate)
        changeFragment(supportFragmentManager.backStackEntryCount)
    }

    override fun eventsTwoFragmentCompleted(eventTime: String, eventPlaceName: String, eventPlaceId: String) {
        eventsViewModel.setEventTime(eventTime)
        eventsViewModel.setEventPlaceName(eventPlaceName)
        eventsViewModel.setEventPlaceId(eventPlaceId)
        changeFragment(supportFragmentManager.backStackEntryCount)
    }

    override fun eventsThreeFragmentCompleted(invitedList: MutableList<String>) {
        eventsViewModel.setInvitedList(invitedList)
        for(string: String in invitedList){
            println(string)
        }
        changeFragment(supportFragmentManager.backStackEntryCount)
    }

    override fun eventsFourFragmentCompleted(eventDescription: String){
        eventsViewModel.setEventDescription(eventDescription)
        eventsViewModel.createEvent()
        finish()
    }
}