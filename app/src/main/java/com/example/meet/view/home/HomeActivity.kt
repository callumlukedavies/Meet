package com.example.meet.view.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.meet.R
import com.example.meet.view.calendar.CalendarFragment
import com.example.meet.view.feed.FeedFragment
import com.example.meet.view.friends.FriendsFragment
import com.example.meet.view.profile.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {

    private var friendsFragment: FriendsFragment? = null
    private val FRIENDS_FRAGMENT_TAG = "friendsFragment"
    private var profileFragment: ProfileFragment? = null
    private val PROFILE_FRAGMENT_TAG = "profileFragment"
    private var feedFragment: FeedFragment? = null
    private val FEED_FRAGMENT_TAG = "feedFragment"
    private var calendarFragment: CalendarFragment? = null
    private val CALENDAR_FRAGMENT_TAG = "calendarFragment"
    private lateinit var CURRENT_TAG: String

//    private lateinit var profileButton: Button
//    private lateinit var friendsButton: Button
//    private lateinit var feedButton: Button
//    private lateinit var calendarButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val actionBar = supportActionBar
//        actionBar?.title = "Friends"
        actionBar?.setDisplayHomeAsUpEnabled(true)

//        friendsButton = findViewById(R.id.friendsFragmentButton)
//        feedButton = findViewById(R.id.feedFragmentButton)
//        profileButton = findViewById(R.id.profileFragmentButton)
//        calendarButton = findViewById(R.id.calendarFragmentButton)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        val navController = navHostFragment.navController

        val bottomNavigationBar = findViewById<BottomNavigationView>(R.id.bottomNavigationBar)
        bottomNavigationBar.setupWithNavController(navController)
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)








//        if(savedInstanceState != null){
//            if(supportFragmentManager.findFragmentByTag(FRIENDS_FRAGMENT_TAG) != null) {
//                friendsFragment = supportFragmentManager
//                        .findFragmentByTag(FRIENDS_FRAGMENT_TAG) as FriendsFragment
//            }else {
//                friendsFragment = FriendsFragment()
//            }
//
//        } else {
//            friendsFragment = FriendsFragment()
//
//            val fragmentTransaction = supportFragmentManager.beginTransaction()
//            fragmentTransaction.add(R.id.homeActivityFragmentContainer, friendsFragment!!, FRIENDS_FRAGMENT_TAG)
//            fragmentTransaction.setReorderingAllowed(true)
//            fragmentTransaction.addToBackStack(FRIENDS_FRAGMENT_TAG)
//            fragmentTransaction.commit()
//            CURRENT_TAG = FRIENDS_FRAGMENT_TAG
//        }

//        profileButton.setOnClickListener {
//            Log.d("HomeActivity", "profileButton Clicked")
//            updateButtonColours(profileButton)
//            actionBar?.title = "Profile"
//
//            val fragmentTransaction = supportFragmentManager.beginTransaction()
//            fragmentTransaction.setReorderingAllowed(true)
//            hideFragment(fragmentTransaction)
//
//            if(profileFragment == null) {
//                profileFragment = ProfileFragment()
//                fragmentTransaction.add(R.id.homeActivityFragmentContainer, profileFragment!!, PROFILE_FRAGMENT_TAG)
//                fragmentTransaction.addToBackStack(PROFILE_FRAGMENT_TAG)
//            } else {
//                supportFragmentManager.popBackStack(PROFILE_FRAGMENT_TAG, 0)
//                fragmentTransaction.show(profileFragment!!)
//            }
//
//            fragmentTransaction.commit()
//        }

//        friendsButton.setOnClickListener {
//            Log.d("HomeActivity", "friendsButton Clicked")
//            updateButtonColours(friendsButton)
//            actionBar?.title = "Friends"
//
//            val fragmentTransaction = supportFragmentManager.beginTransaction()
//            fragmentTransaction.setReorderingAllowed(true)
//            hideFragment(fragmentTransaction)
//            supportFragmentManager.popBackStack(FRIENDS_FRAGMENT_TAG, 0)
//            fragmentTransaction.show(friendsFragment!!)
//
//            fragmentTransaction.commit()
//        }

//        feedButton.setOnClickListener {
//            Log.d("HomeActivity", "feedButton Clicked")
//            updateButtonColours(feedButton)
//            actionBar?.title = "Feed"
//
//            val fragmentTransaction = supportFragmentManager.beginTransaction()
//            fragmentTransaction.setReorderingAllowed(true)
//            hideFragment(fragmentTransaction)
//
//            if(feedFragment == null){
//                feedFragment = FeedFragment()
//                fragmentTransaction.add(R.id.homeActivityFragmentContainer, feedFragment!!, FEED_FRAGMENT_TAG)
//                fragmentTransaction.addToBackStack(FEED_FRAGMENT_TAG)
//            } else {
//                supportFragmentManager.popBackStack(FEED_FRAGMENT_TAG, 0)
//                fragmentTransaction.show(feedFragment!!)
//            }
//
//            fragmentTransaction.commit()
//        }

//        calendarButton.setOnClickListener {
//            Log.d("HomeActivity", "calendarButton Clicked")
//            updateButtonColours(calendarButton)
//            actionBar?.title = "Calendar"
//
//            val fragmentTransaction = supportFragmentManager.beginTransaction()
//            fragmentTransaction.setReorderingAllowed(true)
//            hideFragment(fragmentTransaction)
//
//            if(calendarFragment == null){
//                calendarFragment = CalendarFragment()
//                fragmentTransaction.add(R.id.homeActivityFragmentContainer, calendarFragment!!, CALENDAR_FRAGMENT_TAG)
//                fragmentTransaction.addToBackStack(CALENDAR_FRAGMENT_TAG)
//            } else {
//                fragmentTransaction.show(calendarFragment!!)
//            }
//
//            calendarFragment!!.updateCalendar()
//            fragmentTransaction.commit()
//        }
    }

//    private fun hideFragment(ft: FragmentTransaction){
//        Log.d("HomeActivity", "hideFragment")
//        val count = supportFragmentManager.backStackEntryCount - 1
//        when (supportFragmentManager.getBackStackEntryAt(count).name) {
//            FRIENDS_FRAGMENT_TAG -> {
//                Log.d("HomeActivity", "Hide Friends Fragment")
//                ft.hide(friendsFragment!!)
//            }
//            PROFILE_FRAGMENT_TAG -> {
//                Log.d("HomeActivity", "Hide Profile Fragment")
//                ft.hide(profileFragment!!)
//            }
//            FEED_FRAGMENT_TAG -> {
//                Log.d("HomeActivity", "Hide Feed Fragment")
//                ft.hide(feedFragment!!)
//            }
//            CALENDAR_FRAGMENT_TAG -> {
//                Log.d("HomeActivity", "Hide Calendar Fragment")
//                ft.hide(calendarFragment!!)
//            }
//        }
//    }

//    private fun reverseButtonColours(name: String){
//        when (name) {
//            FRIENDS_FRAGMENT_TAG -> {
//                updateButtonColours(friendsButton)
//            }
//            PROFILE_FRAGMENT_TAG -> {
//                updateButtonColours(profileButton)
//            }
//            FEED_FRAGMENT_TAG -> {
//                updateButtonColours(feedButton)
//            }
//            CALENDAR_FRAGMENT_TAG -> {
//                updateButtonColours(calendarButton)
//            }
//        }
//    }

//    private fun updateButtonColours(button: Button){
//        friendsButton.setBackgroundColor(Color.parseColor("#DDDDDD"))
//        profileButton.setBackgroundColor(Color.parseColor("#DDDDDD"))
//        feedButton.setBackgroundColor(Color.parseColor("#DDDDDD"))
//        calendarButton.setBackgroundColor(Color.parseColor("#DDDDDD"))
//        button.setBackgroundColor(Color.parseColor("#CCCCCC"))
//    }

//    private fun updateActionBarTitle(name: String){
//        when (name) {
//            FRIENDS_FRAGMENT_TAG -> {
//                supportActionBar?.title = "Friends"
//            }
//
//            PROFILE_FRAGMENT_TAG -> {
//                supportActionBar?.title = "Profile"
//            }
//
//            FEED_FRAGMENT_TAG -> {
//                supportActionBar?.title = "Feed"
//            }
//
//            CALENDAR_FRAGMENT_TAG -> {
//                supportActionBar?.title = "Calendar"
//            }
//        }
//    }

//    override fun onBackPressed() {
//        Log.d("HomeActivity", "OnBackPressed")
//        Log.d("HomeActivity", "entry count is ${supportFragmentManager.backStackEntryCount}")
//        if(CURRENT_TAG == supportFragmentManager
//                .getBackStackEntryAt(supportFragmentManager.backStackEntryCount-1).name){
//            //If current tag is the highest thing on the stack, then pop the stack as normal
//            super.onBackPressed()
//            val count = supportFragmentManager.backStackEntryCount - 1
//            val fragmentName = supportFragmentManager.getBackStackEntryAt(count).name
//            updateActionBarTitle(fragmentName!!)
//            reverseButtonColours(fragmentName)
//            if(supportFragmentManager.backStackEntryCount == 0){
//                finish()
//            }
//
//        }else {
//            //hide the current thing and show the thing on the backstack
//            val count = supportFragmentManager.backStackEntryCount - 1
//            val fragmentName = supportFragmentManager.getBackStackEntryAt(count).name
//        }
//    }
}