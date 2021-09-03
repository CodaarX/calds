package com.decagonhq.clads.ui.messages

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.decagonhq.clads.R
import com.decagonhq.clads.data.domain.ChatMessageModel
import com.decagonhq.clads.databinding.FragmentClientChatBinding
import com.decagonhq.clads.ui.BaseFragment
import com.decagonhq.clads.ui.profile.updateToolbarTitleListener
import com.decagonhq.clads.util.EncodeEmail.encodeUserEmail
import com.decagonhq.clads.viewmodels.UserProfileViewModel
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import java.text.SimpleDateFormat
import java.util.*

class ClientChatFragment : BaseFragment() {
    private val args: ClientChatFragmentArgs by navArgs()
    private var _binding: FragmentClientChatBinding? = null
    val binding get() = _binding
    private lateinit var databaseRef: DatabaseReference
    private val adapter = GroupAdapter<ViewHolder>()
    private var mappedUsers: String? = null
    var myId = ""
    var chatterId = ""
    private val userProfileViewModel: UserProfileViewModel by viewModels()


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

        userProfileViewModel.getLocalDatabaseUserProfile()


        receiveMessage()
        binding?.clientChatFragmentRecyclerView?.adapter = adapter
        binding?.clientChatFragmentSendButton?.setOnClickListener {
            sendMessage()
        }

//        val newDbRef = FirebaseDatabase.getInstance().getReference("/user-messages")
//
//        newDbRef.addListenerForSingleValueEvent(object  : ValueEventListener{
//            override fun onDataChange(snapshot: DataSnapshot) {
//                for(userSnapShot in snapshot.children){
//                    if( userSnapShot.key?.contains(myId) == true && userSnapShot.key?.contains(chatterId) == true  ){
//                        mappedUsers = userSnapShot.key
//                    }
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//            }
//
//        })


    }


    private fun sendMessage() {
        val message = binding?.clientChatFragmentTypeMessageEditText?.text.toString()
        val toId = args.clientData?.fromEmail

        userProfileViewModel.userProfile.observe(viewLifecycleOwner, {
            val fromId = it.data?.id
            val fromEmail = encodeUserEmail(it.data?.email)
            val fromName = it.data?.firstName + it.data?.lastName


            val dataSenderBaseRef =
                FirebaseDatabase.getInstance().getReference("/test-messages/$toId/$fromEmail").push()
            val dataReceiverBaseRef =
                FirebaseDatabase.getInstance().getReference("test-messages/$fromEmail/$toId").push()

            Log.d("KEYDATA", "sendMessage: $fromId")

            if (fromId == null) return@observe
            val chatMessage = toId?.let { it1 ->
                ChatMessageModel(dataReceiverBaseRef.key, message, it1, System.currentTimeMillis() / 1000, fromId
                )
            }
            dataSenderBaseRef.setValue(chatMessage)
                .addOnSuccessListener {
                    binding?.clientChatFragmentTypeMessageEditText?.text?.clear()
                    binding?.clientChatFragmentRecyclerView?.scrollToPosition(adapter.itemCount - 1)
                }
            dataReceiverBaseRef.setValue(chatMessage)
        })
        //TODO(Get the unique id between to chatters)


//        var mappedUserId = mappedUsers ?: myId+chatterId

//        if (toId == null) return
//        val chatMessage = ChatMessageModel(message, toId, System.currentTimeMillis() / 1000)
//        databaseRef.setValue(chatMessage)
//            .addOnSuccessListener {
//                binding?.clientChatFragmentTypeMessageEditText?.text?.clear()
//                binding?.clientChatFragmentRecyclerView?.scrollToPosition(adapter.itemCount -1)
//            }

        val latestMessages = FirebaseDatabase.getInstance().getReference("/latest-messages/$toId")
        latestMessages.setValue(chatMessage)
    }

    private fun receiveMessage() {
        val toId = args.clientData?.fromEmail

        userProfileViewModel.userProfile.observe(viewLifecycleOwner, {
            val fromId = it.data?.id
            val fromEmail = encodeUserEmail(it.data?.email)
            val reference = FirebaseDatabase.getInstance().getReference("/test-messages/$toId/$fromEmail")

            reference.addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val chatMessage = snapshot.getValue(ChatMessageModel::class.java)
                    val formatter = SimpleDateFormat("hh:mm a", Locale.getDefault())
                    val calender = Calendar.getInstance()

                    if (chatMessage != null) {
                        if (chatMessage.toId == args.clientData?.fromEmail) {
                            ChatSenderItem(
                                chatMessage.text,
                                formatter.format(calender.time)
                            ).let { adapter.add(it) }
                        } else {
                            ChatReceiverItem(
                                chatMessage.text,
                                formatter.format(calender.time)
                            ).let { adapter.add(it) }
                        }
                    }
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
                override fun onChildRemoved(snapshot: DataSnapshot) {}
                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
                override fun onCancelled(error: DatabaseError) {}

            })
        })

//        val reference = FirebaseDatabase.getInstance().getReference("/user-messages/$toId")
//        reference.addChildEventListener(object : ChildEventListener {
//            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
//                val chatMessage = snapshot.getValue(ChatMessageModel::class.java)
//                val formatter = SimpleDateFormat("hh:mm a", Locale.getDefault())
//                val calender = Calendar.getInstance()
//
//                if (chatMessage != null) {
//                    if (chatMessage.toId == args.clientData?.userId) {
//                        ChatSenderItem(chatMessage.text, formatter.format(calender.time)).let { adapter.add(it) }
//                    } else {
//                        ChatReceiverItem(chatMessage.text, formatter.format(calender.time)).let { adapter.add(it) }
//                    }
//                }
//            }
//
//            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
//            override fun onChildRemoved(snapshot: DataSnapshot) {}
//            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
//            override fun onCancelled(error: DatabaseError) {}
//
//        })
    }

    override fun onResume() {
        super.onResume()
        val fullName = args.clientData?.firstName + " " + args.clientData?.lastName
        (activity as updateToolbarTitleListener).updateTitle(fullName)
    }

}

data class User(val fromId: Int?, val fromEmail: String?, val fromName: String) {
}

class ChatSenderItem(val text: String, val time: String) : Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.findViewById<TextView>(R.id.sender_chat_item_text_view).text = text
        viewHolder.itemView.findViewById<TextView>(R.id.sender_time_text_view).text = time
    }

    override fun getLayout(): Int {
        return R.layout.sender_chat_item
    }
}

class ChatReceiverItem(val text: String, val time: String) : Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.findViewById<TextView>(R.id.receiver_chat_item_text_view).text = text
        viewHolder.itemView.findViewById<TextView>(R.id.receiver_time_text_view).text = time
    }

    override fun getLayout(): Int {
        return R.layout.receiver_chat_item
    }
}