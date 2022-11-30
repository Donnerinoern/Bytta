package prj.edu.bytta.Chat

import java.util.*

data class Brukern(
    val navn: String,
    val profileImageUrl: String,
    val isOnline: Boolean,
    val lastOnline: Date,
    val uid: String,

    )
