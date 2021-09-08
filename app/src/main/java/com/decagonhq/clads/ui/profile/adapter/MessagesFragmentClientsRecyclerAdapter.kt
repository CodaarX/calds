package com.decagonhq.clads.ui.profile.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.decagonhq.clads.data.domain.ChatMessageModel
import com.decagonhq.clads.data.domain.MessagesNotificationModel
import com.decagonhq.clads.databinding.ChatRecyclerviewItemBinding
import com.decagonhq.clads.ui.messages.MessagesFragmentDirections
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import kotlin.collections.HashMap

class MessagesFragmentClientsRecyclerAdapter(
    private var messageNotificationList: List<MessagesNotificationModel>
) :
    RecyclerView.Adapter<MessagesFragmentClientsRecyclerAdapter.ViewHolder>() {

    val latestMessagesHashMap = HashMap<String, ChatMessageModel>()

    inner class ViewHolder(val binding: ChatRecyclerviewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val clientName = binding.chatRecyclerViewItemClientNameTextView
        val notificationBody = binding.chatRecyclerViewItemMessageTextView
    }

    /*inflate the views*/
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ChatRecyclerviewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    /*return the messageNotificationList*/
    override fun getItemCount(): Int {
        return messageNotificationList.size
    }

    /*bind the views with their holders*/
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.itemView.apply {
            with(holder) {
                with(messageNotificationList[position]) {
                    val clientFullName = "$firstName $lastName"
                    clientName.text = clientFullName
                    notificationBody.text = text

//                    val clientInitials = clientFullName.split(" ")[0].substring(0, 1).capitalize(
//                        Locale.ROOT
//                    ) + clientFullName.split(" ")[1].substring(0, 1).capitalize(Locale.ROOT)
//                    val color =
//                        ColorSelector.selectColorByCharacter((clientFullName.split(" ")[0].first()))
//                    val drawable = TextDrawable.builder().beginConfig()
//                        .width(150)
//                        .height(150)
//                        .fontSize(55)
//                        .endConfig()
//                        .buildRound(clientInitials, color)

//                    binding.chatRecyclerViewItemImageView.setImageDrawable(drawable)
                    binding.chatRecyclerViewItemParentLayout.setOnClickListener {
                        val client = messageNotificationList[position]
                        val action =
                            MessagesFragmentDirections.actionNavMessagesToClientChatFragment2(client)
                        findNavController().navigate(action)
                    }
                    val toId = messageNotificationList[position].fromEmail
                    val reference =
                        FirebaseDatabase.getInstance().getReference("/latest-messages/$toId")

                    reference.addChildEventListener(object : ChildEventListener {
                        override fun onChildAdded(
                            snapshot: DataSnapshot,
                            previousChildName: String?
                        ) {

                            val chatMessage = snapshot.getValue(ChatMessageModel::class.java) ?: return
                            binding.chatRecyclerViewItemMessageTextView.text = chatMessage.text
                            binding.chatRecyclerViewItemTimeTextView.text = chatMessage.timeStamp
                            latestMessagesHashMap[snapshot.key!!] = chatMessage
//                            refreshRecyclerViewMessages()
                        }

                        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                            val chatMessage = snapshot.getValue(ChatMessageModel::class.java) ?: return
                            binding.chatRecyclerViewItemMessageTextView.text = chatMessage.text
                            binding.chatRecyclerViewItemTimeTextView.text = chatMessage.timeStamp
                            latestMessagesHashMap[snapshot.key!!] = chatMessage
                        }

                        override fun onChildRemoved(snapshot: DataSnapshot) {}

                        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

                        override fun onCancelled(error: DatabaseError) {}
                    })
                }
            }
        }
    }
}
