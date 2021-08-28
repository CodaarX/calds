package com.decagonhq.clads.ui.profile.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.amulyakhare.textdrawable.TextDrawable
import com.decagonhq.clads.data.domain.MessagesNotificationModel
import com.decagonhq.clads.databinding.ChatRecyclerviewItemBinding
import com.decagonhq.clads.ui.messages.MessagesFragmentDirections
import com.decagonhq.clads.util.ColorSelector
import java.util.*

class MessagesFragmentClientsRecyclerAdapter(
    private var messageNotificationList: ArrayList<MessagesNotificationModel>
) :
    RecyclerView.Adapter<MessagesFragmentClientsRecyclerAdapter.ViewHolder>() {

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
                    notificationBody.text = body

                    val clientInitials = clientFullName.split(" ")[0].substring(0, 1).capitalize(
                        Locale.ROOT
                    ) +
                            clientFullName.split(" ")[1].substring(0, 1).capitalize(Locale.ROOT)
                    val color =
                        ColorSelector.selectColorByCharacter((clientFullName.split(" ")[0].first()))
                    val drawable = TextDrawable.builder().beginConfig()
                        .width(150)
                        .height(150)
                        .fontSize(55)
                        .endConfig()
                        .buildRound(clientInitials, color)

                    binding.chatRecyclerViewItemImageView.setImageDrawable(drawable)
                    binding.chatRecyclerViewItemParentLayout.setOnClickListener {
                        val client = messageNotificationList[position]
                        val action = MessagesFragmentDirections.actionNavMessagesToClientChatFragment2(client)
                        findNavController().navigate(action)
                    }
                }
            }
        }
    }
}

