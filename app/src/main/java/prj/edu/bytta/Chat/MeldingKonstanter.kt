package prj.edu.bytta

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

object MeldingKonstanter {
        const val TAG = "Bytta_Chat"

        const val MELDINGER = "msg"
        const val MELDING = "melding"
        const val SENDT_AV = "avsender"
        const val SENDT = "tidspunkt"
        const val BRUKER = "brukeren"

}
// Konstanter for chatmeldinger og laging av kontakter
class ChatKonstanter(
        val id: String,
        val text: String,
        val fromId: String,
        val toId: String,
        val timestamp: String,
        val time: Long
) {
        constructor(): this("","","","","",-1)

        var imageUrl: String? = null
        var fileUrl: String? = null
        var fileSize: Double? = null
        var fileType: String? = null

        //Bilde melding
        constructor(id:String, text: String, fromId: String, toId: String, timestamp: String,
        time: Long, imageUrl: String) : this (id, text, fromId, toId, timestamp, time) {
                this.imageUrl = imageUrl
        }

        constructor(id: String, text: String, fromId: String, toId: String, timestamp: String, time: Long, fileUrl: String, fileSize: Double, fileType: String) : this(id, text, fromId, toId, timestamp, time) {
                this.fileUrl = fileUrl
                this.fileSize = fileSize
                this.fileType = fileType
        }
}

class User(val uid: String, var username: String, var profileImageUrl: String, var email: String, var token: String?) {
        constructor() : this("","","","","")
}





