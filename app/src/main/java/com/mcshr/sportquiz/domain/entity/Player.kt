package com.mcshr.sportquiz.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Player(
    val name:String,
    val score:Int
): Parcelable