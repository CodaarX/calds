package com.decagonhq.clads.data.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

class ChatMessageModel(val id: String? = null, val text: String, val toId: String, val timeStamp: String, val fromId: Int? = null, val userName: String? = null) {
    constructor() : this("", "", "1", "", 1)
}

@Parcelize
class MessageModel(val firstName: String? = null, val lastName: String? = null, val timeStamp: Long? = null, val text: String? = null, val userId: String? = null,) :
    Parcelable {
    constructor() : this("", "", -1, "", "")
}
