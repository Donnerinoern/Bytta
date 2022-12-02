package prj.edu.bytta.chat

import androidx.annotation.Keep
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


object FirestoreKonstanter {
        // Bruker collection

        const val BRUKER_COLLECTION = "brukere"
        const val BRUKER_UID = "uid"
        const val BRUKER_NAVN ="brukerNavn"
        const val BRUKER_KONTAKTER = "kontakter"
        const val BRUKER_MELDING_TOKEN = "meldingToken"


        //Chatroom collection

        const val CHATROM_COLLECTION = "chatterom"
        const val CHATROM_UID = "chatsUid"
        const val CHATROM_NAVN = "chatteromNavn"
        const val CHATROM_BRUKERE = "chatromBrukere"
        const val CHATROM_MELDINGER = "chatromMeldinger"
        const val CHATROM_ADMIN = "admin"
        const val CHATROM_SISTE_MELDING_TID ="sistMeldingTid"
        const val CHATROM_SISTE_MELDING_TEXT = "sisteMeldingText"
        const val CHATROM_GRUPPE = "gruppe"

        // Sub collection av Collection-meldinger

        const val MELDING_COLLECTION = "meldinger"
        const val MELDING_UID = "meldingUid"
        const val MELDING_TEXT = "meldingText"
        const val MELDING_SENDT_TID = "tidspunktet"
        const val MELDING_SENDER_UID = "senderUid"
        const val MELDING_AVSENDER_NAVN = "avsenderNavn"

}

object GruppeLimit {
        const val MAX_BRUKERE = 2
}

@Keep
object ChatStartedEvent

@Keep
object FriendsChangedEvent

@Keep
object GroupsChangedEvent

@Keep
class ChatRoomChangedEvent(val chatteRomUid: String)





