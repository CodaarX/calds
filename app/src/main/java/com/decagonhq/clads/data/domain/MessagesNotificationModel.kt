package com.decagonhq.clads.data.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class MessagesNotificationModel(
    var firstName: String,
    var lastName: String,
    var day: String,
    var body: String,
    var userId: String

    ) : Parcelable
