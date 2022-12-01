package prj.edu.bytta.meldinger

import prj.edu.bytta.Bruker
import java.util.*

data class DisplayChatteRommet(

    var chatUid: String = "",

    var gruppe: Boolean = false,

    var chatteRomNavn: String = "",

    var antallIRommet: Int = 0,

    var visBrukerNavn: String = "",

    var sisteMeldingTid: Date? = null,

    var sisteMeldingText: String? = null,
        )


fun mergeChatteromAndBruker(
    chatteRoms: List<ChatteRom>,
    brukere: List<Bruker>,
    onCompletion: (List<DisplayChatteRommet>) -> Unit,

    ) {
    val displayChatteRoms = mutableListOf<DisplayChatteRommet>()
    if (chatteRoms.size != brukere.size) {
        onCompletion(displayChatteRoms)
        return
    }
    for (i in chatteRoms.indices) {
     if (chatteRoms[i].gruppe) {
         val displayChatteRommet = DisplayChatteRommet(
             chatUid = chatteRoms[i].chatUid,
             chatteRomNavn = chatteRoms[i].chatRomNavn,
             gruppe = chatteRoms[i].gruppe,
             visBrukerNavn = brukere[i].brukerNavn,
             antallIRommet = chatteRoms[i].chatRomBrukere.size,
             sisteMeldingText = chatteRoms[i].sisteMeldingText,
             sisteMeldingTid = chatteRoms[i].sisteMeldingTid,
         )
         displayChatteRoms.add(displayChatteRommet)
         if (displayChatteRoms.size >= chatteRoms.size) {
             onCompletion(displayChatteRoms)
         }
     }
    }

}