package prj.edu.bytta.meldinger

import com.google.firebase.database.PropertyName
import prj.edu.bytta.FirestoreKonstanter
import java.util.*

data class Melding(

    @PropertyName(FirestoreKonstanter.MELDING_UID)
    var meldingUid: String = createUid(),

    @PropertyName(FirestoreKonstanter.MELDING_TEXT)
    var meldingText: String = "",

    @PropertyName(FirestoreKonstanter.MELDING_SENDT_TID)
    var meldingTid: Date = Date(),

    @PropertyName(FirestoreKonstanter.MELDING_AVSENDER_NAVN)
    var senderNavn: String = "",

    @PropertyName(FirestoreKonstanter.MELDING_SENDER_UID)
    var senderUid: String = "",

) : List<Melding>


