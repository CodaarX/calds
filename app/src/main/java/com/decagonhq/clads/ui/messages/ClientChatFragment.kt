package com.decagonhq.clads.ui.messages

import android.os.Bundle
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
import com.decagonhq.clads.viewmodels.FireBaseViewModel
import com.decagonhq.clads.viewmodels.UserProfileViewModel
import com.google.firebase.database.DatabaseReference
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
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
    private val firebaseViewModel: FireBaseViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
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

        firebaseViewModel.chatSenderItem.observe(
            viewLifecycleOwner,
            {
                adapter.add(it)
                binding?.clientChatFragmentRecyclerView?.scrollToPosition(adapter.itemCount - 1)
            }
        )

        firebaseViewModel.chatReceiverItem.observe(
            viewLifecycleOwner,
            {
                adapter.add(it)
                binding?.clientChatFragmentRecyclerView?.scrollToPosition(adapter.itemCount - 1)
            }
        )

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
        // initialize text views
        val message = binding?.clientChatFragmentTypeMessageEditText?.text.toString()

        // get user profile from args (from message to chat screen)
        val toId = args.clientData?.fromEmail

        // observe view model for user profile
        userProfileViewModel.userProfile.observe(
            viewLifecycleOwner,
            {
                // get user ID and Email from this.User
                val fromEmail = encodeUserEmail(it.data?.email)
                val formatter = SimpleDateFormat("hh:mm a", Locale.getDefault())
                val calender = Calendar.getInstance()

                if (toId == null) return@observe // check if id is null

                // get the entire message of the  sender
                val chatMessage = toId?.let { it1 ->
                    ChatMessageModel(message, toId, formatter.format(calender.time), fromEmail!!)
                }

                firebaseViewModel.sendMessages(chatMessage, fromEmail!!, toId)

                binding?.clientChatFragmentTypeMessageEditText?.text?.clear()
                binding?.clientChatFragmentRecyclerView?.scrollToPosition(adapter.itemCount - 1)
            }
        )

//        var mappedUserId = mappedUsers ?: myId+chatterId

//        if (toId == null) return
//        val chatMessage = ChatMessageModel(message, toId, System.currentTimeMillis() / 1000)
//        databaseRef.setValue(chatMessage)
//            .addOnSuccessListener {
//                binding?.clientChatFragmentTypeMessageEditText?.text?.clear()
//                binding?.clientChatFragmentRecyclerView?.scrollToPosition(adapter.itemCount -1)
//            }
    }

//    private fun refreshRecyclerViewMessages () {
//        latestMessagesHashMap.values.forEach{
//
//        }

    private fun receiveMessage() {
        val toId = args.clientData?.fromEmail

        userProfileViewModel.userProfile.observe(
            viewLifecycleOwner,
            {
                val fromEmail = encodeUserEmail(it.data?.email)

                firebaseViewModel.receiveMessages(toId!!, fromEmail!!, args.clientData!!)
            }
        )
    }

    override fun onResume() {
        super.onResume()
        val fullName = args.clientData?.firstName + " " + args.clientData?.lastName
        (activity as updateToolbarTitleListener).updateTitle(fullName)
    }
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
