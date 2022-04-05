package com.example.meet.view.home

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.example.meet.R
import com.example.meet.view.calendar.CalendarFragment
import com.example.meet.view.feed.FeedFragment
import com.example.meet.view.friends.FriendsFragment
import com.example.meet.view.profile.ProfileFragment
import java.util.*

class HomeActivity : AppCompatActivity() {

    private var friendsFragment: FriendsFragment? = null
    private val FRIENDS_FRAGMENT_TAG = "friendsFragment"
    private var profileFragment: ProfileFragment? = null
    private val PROFILE_FRAGMENT_TAG = "profileFragment"
    private var feedFragment: FeedFragment? = null
    private val FEED_FRAGMENT_TAG = "feedFragment"
    private var calendarFragment: CalendarFragment? = null
    private val CALENDAR_FRAGMENT_TAG = "calendarFragment"

    private lateinit var CURRENT_FRAGMENT_TAG: String
    private lateinit var profileButton: Button
    private lateinit var friendsButton: Button
    private lateinit var feedButton: Button
    private lateinit var calendarButton: Button
    private lateinit var stack: Stack<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val actionBar = supportActionBar
        actionBar?.title = "Friends"
        actionBar?.setDisplayHomeAsUpEnabled(false)

        friendsButton = findViewById(R.id.friendsFragmentButton)
        feedButton = findViewById(R.id.feedFragmentButton)
        profileButton = findViewById(R.id.profileFragmentButton)
        calendarButton = findViewById(R.id.calendarFragmentButton)
        stack = Stack()

        if(savedInstanceState != null){
            if(supportFragmentManager.findFragmentByTag(FRIENDS_FRAGMENT_TAG) != null) {
                friendsFragment = supportFragmentManager
                        .findFragmentByTag(FRIENDS_FRAGMENT_TAG) as FriendsFragment
                CURRENT_FRAGMENT_TAG = FRIENDS_FRAGMENT_TAG
                stack.push(FRIENDS_FRAGMENT_TAG)
            }else {
                friendsFragment = FriendsFragment()
                CURRENT_FRAGMENT_TAG = FRIENDS_FRAGMENT_TAG
                stack.push(FRIENDS_FRAGMENT_TAG)
            }

        } else {
            friendsFragment = FriendsFragment()

            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.add(R.id.homeActivityFragmentContainer, friendsFragment!!, FRIENDS_FRAGMENT_TAG)
            fragmentTransaction.addToBackStack(FRIENDS_FRAGMENT_TAG)
            fragmentTransaction.commit()
            CURRENT_FRAGMENT_TAG = FRIENDS_FRAGMENT_TAG
            stack.push(FRIENDS_FRAGMENT_TAG)
        }

        profileButton.setOnClickListener {
            if(profileFragment == null){
                profileFragment = ProfileFragment()
                val fragmentTransaction = supportFragmentManager.beginTransaction()
                hideFragment(fragmentTransaction)
                fragmentTransaction.add(R.id.homeActivityFragmentContainer, profileFragment!!, PROFILE_FRAGMENT_TAG)
                fragmentTransaction.addToBackStack(PROFILE_FRAGMENT_TAG)
                fragmentTransaction.commit()
                CURRENT_FRAGMENT_TAG = PROFILE_FRAGMENT_TAG
                stack.push(PROFILE_FRAGMENT_TAG)
            } else {
                val fragmentTransaction = supportFragmentManager.beginTransaction()
                hideFragment(fragmentTransaction)
                fragmentTransaction.show(profileFragment!!)
                fragmentTransaction.commit()
                CURRENT_FRAGMENT_TAG = PROFILE_FRAGMENT_TAG
                stack.push(PROFILE_FRAGMENT_TAG)
            }
            updateButtonColours(profileButton)
            actionBar?.title = "Profile"
        }

        friendsButton.setOnClickListener {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            hideFragment(fragmentTransaction)
            fragmentTransaction.show(friendsFragment!!)
            fragmentTransaction.commit()
            CURRENT_FRAGMENT_TAG = FRIENDS_FRAGMENT_TAG
            stack.push(FRIENDS_FRAGMENT_TAG)
            updateButtonColours(friendsButton)
            actionBar?.title = "Friends"
        }

        feedButton.setOnClickListener {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            if(feedFragment == null){
                feedFragment = FeedFragment()
                hideFragment(fragmentTransaction)
                fragmentTransaction.add(R.id.homeActivityFragmentContainer, feedFragment!!, FEED_FRAGMENT_TAG)
                fragmentTransaction.addToBackStack(FEED_FRAGMENT_TAG)
                fragmentTransaction.commit()
            } else {
                hideFragment(fragmentTransaction)
                fragmentTransaction.show(feedFragment!!)
                fragmentTransaction.commit()
            }

            CURRENT_FRAGMENT_TAG = FEED_FRAGMENT_TAG
            stack.push(FEED_FRAGMENT_TAG)
            updateButtonColours(feedButton)
            actionBar?.title = "Feed"
        }

        calendarButton.setOnClickListener {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            if(calendarFragment == null){
                calendarFragment = CalendarFragment()
                hideFragment(fragmentTransaction)
                fragmentTransaction.add(R.id.homeActivityFragmentContainer, calendarFragment!!, CALENDAR_FRAGMENT_TAG)
                fragmentTransaction.addToBackStack(CALENDAR_FRAGMENT_TAG)
                fragmentTransaction.commit()
            } else {
                hideFragment(fragmentTransaction)
                fragmentTransaction.show(calendarFragment!!)
                fragmentTransaction.commit()
            }


            CURRENT_FRAGMENT_TAG = CALENDAR_FRAGMENT_TAG
            stack.push(CALENDAR_FRAGMENT_TAG)
            updateButtonColours(calendarButton)
            calendarFragment!!.updateCalendar()
            actionBar?.title = "Calendar"
        }
    }

    private fun hideFragment(ft: FragmentTransaction){
        when (stack.peek()) {
            FRIENDS_FRAGMENT_TAG -> {
                ft.hide(friendsFragment!!)
            }
            PROFILE_FRAGMENT_TAG -> {
                ft.hide(profileFragment!!)
            }
            FEED_FRAGMENT_TAG -> {
                ft.hide(feedFragment!!)
            }
            CALENDAR_FRAGMENT_TAG -> {
                ft.hide(calendarFragment!!)
            }
        }
    }

    private fun reverseButtonColours(){
        when (stack.peek()) {
            FRIENDS_FRAGMENT_TAG -> {
                updateButtonColours(friendsButton)
            }
            PROFILE_FRAGMENT_TAG -> {
                updateButtonColours(profileButton)
            }
            FEED_FRAGMENT_TAG -> {
                updateButtonColours(feedButton)
            }
            CALENDAR_FRAGMENT_TAG -> {
                updateButtonColours(calendarButton)
            }
        }
    }

    private fun updateButtonColours(button: Button){
        friendsButton.setBackgroundColor(Color.parseColor("#DDDDDD"))
        profileButton.setBackgroundColor(Color.parseColor("#DDDDDD"))
        feedButton.setBackgroundColor(Color.parseColor("#DDDDDD"))
        calendarButton.setBackgroundColor(Color.parseColor("#DDDDDD"))
        button.setBackgroundColor(Color.parseColor("#CCCCCC"))
    }

    override fun onBackPressed() {
        super.onBackPressed()
        println("OnBackPressed")
        println(supportFragmentManager.backStackEntryCount)
        stack.pop()
        reverseButtonColours()
        if(supportFragmentManager.backStackEntryCount == 0){
            finish()
        }
    }
}