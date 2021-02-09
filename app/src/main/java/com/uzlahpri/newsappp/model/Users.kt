package com.uzlahpri.newsappp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Users(
    private var uid : String = "0",
    private var fullName : String = "",
    private var email : String = "",
    private var linkedIn : String = "",
    private var instagram : String = "",
    private var medium : String = "",
    private var photo : String = ""
): Parcelable
