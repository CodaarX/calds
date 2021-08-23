package com.decagonhq.clads.data.domain.profile

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ArtisanAddress(
    val locality: String? = null,
    val featureName: String? = null,
    val thoroughfare: String? = null,
    val artisanState: String? = null,
    val subAdminArea: String? = null,
    val artisanLatitude: Double = 0.0,
    val artisanLongitude: Double =0.0,
) : Parcelable