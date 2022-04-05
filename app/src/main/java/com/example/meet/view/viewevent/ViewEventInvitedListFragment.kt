package com.example.meet.view.viewevent

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.meet.R
import com.example.meet.model.User
import com.example.meet.viewmodel.ViewEventViewModel

class ViewEventInvitedListFragment(private var viewEventViewModel: ViewEventViewModel) : Fragment() {

    private lateinit var onEventListener: OnEventListener

    interface OnEventListener {
        fun returnToMainFragmentFromInvitedList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_event_invited_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val invitedList = mutableListOf<User>()

        val fragmentImageView = view.findViewById<ImageView>(R.id.viewEventFragmentImageView)
        val linearLayoutManager = LinearLayoutManager(context)

        val fragmentRecyclerView = view.findViewById<RecyclerView>(R.id.viewEventFragmentRecyclerView)
        val invitedListAdapter = InvitedListAdapter(invitedList)
        fragmentRecyclerView.layoutManager = linearLayoutManager
        fragmentRecyclerView.adapter = invitedListAdapter


        viewEventViewModel.getInvitedListMutableLiveData().observe(viewLifecycleOwner, object :
            Observer<MutableList<User>> {
            override fun onChanged(t: MutableList<User>) {
                invitedListAdapter.setData(t)
            }
        })

        fragmentImageView.setOnClickListener{
            onEventListener.returnToMainFragmentFromInvitedList()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is OnEventListener){
            onEventListener = context
        } else {
            throw ClassCastException(context.toString()
                    + "has not implemented interface ViewEventMainFragment.OnEventListener")
        }
    }
}