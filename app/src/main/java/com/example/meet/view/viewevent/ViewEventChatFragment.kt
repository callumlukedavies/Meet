package com.example.meet.view.viewevent

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.meet.R
import com.example.meet.model.Message
import com.example.meet.viewmodel.ViewEventViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [ViewEventChatFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ViewEventChatFragment(private var viewEventViewModel: ViewEventViewModel) : Fragment() {

    private lateinit var onEventListener: ViewEventChatFragment.OnEventListener

    interface OnEventListener {
        fun returnToMainFragmentFromChat()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_event_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val messagesList = mutableListOf<Message>()
        val fragmentImageView = view.findViewById<ImageView>(R.id.viewEventFragmentImageView)
        val messagesAdapter = MessagesAdapter(messagesList, viewEventViewModel.getFirebaseAuthUID())
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.stackFromEnd = true

        val fragmentRecyclerView = view.findViewById<RecyclerView>(R.id.viewEventFragmentRecyclerView)
        val viewEventEditText = view.findViewById<EditText>(R.id.viewEventEditText)
        val sendButton = view.findViewById<Button>(R.id.viewEventFragmentSendButton)

        fragmentRecyclerView.adapter = messagesAdapter
        fragmentRecyclerView.layoutManager = linearLayoutManager

        sendButton.setOnClickListener {
            val messageText = viewEventEditText.text.toString()
            if(messageText.isNotEmpty()) viewEventViewModel.sendMessageToEventChat(messageText)
            viewEventEditText.setText("")
        }

        viewEventViewModel.getMessagesListMutableLiveData().observe(viewLifecycleOwner
        ) {
            messagesAdapter.setData(it)
        }

        fragmentImageView.setOnClickListener{
            onEventListener.returnToMainFragmentFromChat()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is OnEventListener){
            onEventListener = context
        } else {
            throw ClassCastException(context.toString()
                    + "has not implemented interface ViewEventChatFragment.OnEventListener")
        }
    }
}