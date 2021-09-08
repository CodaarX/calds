package com.decagonhq.clads.ui.messages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.decagonhq.clads.data.domain.ChatMessageModel
import com.decagonhq.clads.data.domain.MessagesNotificationModel
import com.decagonhq.clads.databinding.MessagesFragmentBinding
import com.decagonhq.clads.ui.BaseFragment
import com.decagonhq.clads.ui.profile.adapter.MessagesFragmentClientsRecyclerAdapter
import com.decagonhq.clads.util.EncodeEmail
import com.decagonhq.clads.viewmodels.ClientViewModel
import com.decagonhq.clads.viewmodels.UserProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import timber.log.Timber

class MessagesFragment : BaseFragment() {

    private var _binding: MessagesFragmentBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!
    private lateinit var notificationRecyclerView: RecyclerView
    private lateinit var notificationAdapter: MessagesFragmentClientsRecyclerAdapter
    private var userArrayList: ArrayList<MessagesNotificationModel> = arrayListOf()
    private val clientViewModel: ClientViewModel by activityViewModels()
    private val userProfileViewModel: UserProfileViewModel by activityViewModels()
    private val adapter = GroupAdapter<ViewHolder>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = MessagesFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        getNotification()

        userProfileViewModel.userProfile.observe(viewLifecycleOwner) {
            val userEmail = EncodeEmail.encodeUserEmail(it.data?.email)
            getClient(userEmail)
        }

        notificationRecyclerView = binding.messagesFragmentClientMessagesRecyclerView
        notificationRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }


    private fun getClient(userEmail: String?) {

        val firebaseRef = FirebaseDatabase.getInstance().getReference("/users")

        firebaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userArrayList.clear()
                Timber.e(userEmail)
                if (snapshot.exists()) {
                    val userList =
                        snapshot.children.mapNotNull {
                            it.getValue(MessagesNotificationModel::class.java)
                        }.filter {
                            it.fromEmail != userEmail && !it.firstName.isNullOrEmpty()
                        }
//                    snapshot.children.forEach {
//
//                        val user = it.getValue(MessagesNotificationModel::class.java)
//                        if (user != null && user.fromEmail != userEmail ) {
//                                userArrayList.add(user)
//
//                        }
//                        Timber.e(user.toString())
//                    }
                    notificationAdapter = MessagesFragmentClientsRecyclerAdapter(userList)
                    Timber.e(userList.toString())
                    notificationRecyclerView.adapter = notificationAdapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    private fun getNotification() {
        userArrayList = arrayListOf(
            MessagesNotificationModel(
                "Lorem Ipsum",
                "Ola",
                "Michavelli",
                "yesterday",
                1
            ),
            MessagesNotificationModel(
                "My name is Ruth. I need a dress for sunday",
                "Ruth",
                "Unoka",
                "Today",
                2
            ),
            MessagesNotificationModel(
                "Hi, I have an event next month",
                "Michael",
                "Isesele",
                "Today",
                3
            ),
            MessagesNotificationModel(
                "Lorem Ipsum",
                "John",
                "Ose",
                "Lorem Ipsum",
                4
            ),
            MessagesNotificationModel(
                "Hello there",
                "Tolulope",
                "Lo",
                "Today",
                5
            ),
            MessagesNotificationModel(
                "Hi, I have an event next month",
                "Tomisin",
                "Ade",
                "Last Month",
                6
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


