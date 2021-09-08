package com.decagonhq.clads.data.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

class ChatMessageModel(val text: String, val toId: String, val timeStamp: String, val fromId: String) {
    constructor() : this("", "", "", "")
}

@Parcelize
class MessageModel(val firstName: String? = null, val lastName: String? = null, val timeStamp: Long? = null, val text: String? = null, val userId: String? = null,) :
    Parcelable {
    constructor() : this("", "", -1, "", "")
}
