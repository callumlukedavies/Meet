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


/**
 * A simple [Fragment] subclass.
 * Use the [ViewEventAttendingListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ViewEventAttendingListFragment(private var viewEventViewModel: ViewEventViewModel) : Fragment() {

    private lateinit var onEventListener: OnEventListener

    interface OnEventListener {
        fun returnToMainFragmentFromAttendingList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_event_attending_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val attendingList = mutableListOf<User>()

        val fragmentImageView = view.findViewById<ImageView>(R.id.viewEventFragmentImageView)
        val linearLayoutManager = LinearLayoutManager(context)

        val fragmentRecyclerView = view.findViewById<RecyclerView>(R.id.viewEventFragmentRecyclerView)
        val attendingListAdapter = AttendingListAdapter(attendingList)
        fragmentRecyclerView.layoutManager = linearLayoutManager
        fragmentRecyclerView.adapter = attendingListAdapter


        viewEventViewModel.getAttendingListMutableLiveData().observe(viewLifecycleOwner, object :
            Observer<MutableList<User>> {
            override fun onChanged(t: MutableList<User>) {
                attendingListAdapter.setData(t)
            }
        })

        fragmentImageView.setOnClickListener{
            onEventListener.returnToMainFragmentFromAttendingList()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is OnEventListener){
            onEventListener = context
        } else {
            throw ClassCastException(context.toString()
                    + "has not implemented interface ViewEventAttendingListFragment.OnEventListener")
        }
    }
}