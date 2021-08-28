package com.decagonhq.clads.ui.messages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.decagonhq.clads.R
import com.decagonhq.clads.databinding.FragmentClientChatBinding
import com.decagonhq.clads.ui.profile.updateToolbarTitleListener
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder

class ClientChatFragment : Fragment() {
    private val args: ClientChatFragmentArgs by navArgs()
    private var _binding: FragmentClientChatBinding? = null
    val binding get() = _binding
    private lateinit var database: DatabaseReference
    private val adapter = GroupAdapter<ViewHolder>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentClientChatBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        receiveMessage()
        binding?.clientChatFragmentRecyclerView?.adapter = adapter
        binding?.clientChatFragmentSendButton?.setOnClickListener {
            sendMessage()
        }


    }

    class ChatMessage(val text: String, val toId: String, val timeStamp: Long) {
        constructor() : this("", "", -1)
    }


    private fun sendMessage() {
        val message = binding?.clientChatFragmentTypeMessageEditText?.text.toString()
        val toId = args.clientData?.userId
        database = FirebaseDatabase.getInstance().getReference("/user-messages/$toId").push()

        if (toId == null) return
        val chatMessage = ChatMessage(message, toId, System.currentTimeMillis() / 1000)
        database.setValue(chatMessage)
            .addOnSuccessListener {
                binding?.clientChatFragmentTypeMessageEditText?.text?.clear()
            }
    }

    private fun receiveMessage() {
        val toId = args.clientData?.userId
        val reference = FirebaseDatabase.getInstance().getReference("/user-messages/$toId")
        reference.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage = snapshot.getValue(ChatMessage::class.java)
                if (chatMessage != null) {
                    if (chatMessage.toId == args.clientData?.userId) {
                        ChatSenderItem(chatMessage.text).let { adapter.add(it) }
                    } else {
                        ChatReceiverItem(chatMessage.text).let { adapter.add(it) }
                    }
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    override fun onResume() {
        super.onResume()
        val fullName = args.clientData?.firstName + " " + args.clientData?.lastName
        (activity as updateToolbarTitleListener).updateTitle(fullName)
    }

}

class ChatSenderItem(val text: String) : Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.findViewById<TextView>(R.id.sender_chat_item_text_view).text = text
    }

    override fun getLayout(): Int {
        return R.layout.sender_chat_item
    }
}

class ChatReceiverItem(val text: String) : Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.findViewById<TextView>(R.id.receiver_chat_item_text_view).text = text
    }

    override fun getLayout(): Int {
        return R.layout.receiver_chat_item
    }
}