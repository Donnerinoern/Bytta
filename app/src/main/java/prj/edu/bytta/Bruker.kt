package prj.edu.bytta

import com.google.firebase.database.PropertyName
import prj.edu.bytta.chat.FirestoreKonstanter

data class Bruker(

    @PropertyName(FirestoreKonstanter.BRUKER_UID)
    var uid: String = "",

    @PropertyName(FirestoreKonstanter.BRUKER_NAVN)
    var brukerNavn: String = "",

    @PropertyName(FirestoreKonstanter.BRUKER_KONTAKTER)
    var kontakter: List<String> = listOf(),

    @PropertyName(FirestoreKonstanter.BRUKER_MELDING_TOKEN)
    var meldingToken: String = ""
)
