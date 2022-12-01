package prj.edu.bytta.meldinger

import com.google.firebase.database.PropertyName
import prj.edu.bytta.FirestoreKonstanter
import java.util.*

data class ChatteRom(

    @PropertyName(FirestoreKonstanter.CHATROM_UID)
    var chatUid: String = "",

    @PropertyName(FirestoreKonstanter.CHATROM_NAVN)
    var chatRomNavn: String = "",

    @PropertyName(FirestoreKonstanter.CHATROM_BRUKERE)
    var chatRomBrukere: List<String> = listOf(),

    @PropertyName(FirestoreKonstanter.CHATROM_ADMIN)
    var admin: String? = null,

    @PropertyName(FirestoreKonstanter.CHATROM_SISTE_MELDING_TID)
    var sisteMeldingTid: Date? = null,

    @PropertyName(FirestoreKonstanter.CHATROM_SISTE_MELDING_TEXT)
    var sisteMeldingText: String? = null,

    @PropertyName(FirestoreKonstanter.CHATROM_GRUPPE)
    var gruppe: Boolean = false
)
