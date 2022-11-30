package prj.edu.bytta.Chat

import java.util.Date

data class Melding(
    val user: Brukern,
    val text: String,
    val imageUrl: String? = null,
    val time: Date,
    val userId: String,
    val toId: String,
)
