package prj.edu.bytta.meldinger

import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import prj.edu.bytta.FirestoreKonstanter
import javax.inject.Inject

class ChatRoomRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    // Finner/Henter ett Chatterom fra Firestore, sjekker også om en chat eksisterer
    fun getChatRoom(chatsUid: String): Task<DocumentSnapshot> {
        return firestore
            .collection(FirestoreKonstanter.CHATROM_UID)
            .document(chatsUid)
            .get()
    }

    //Finner alle chatterommene brukeren er med i
    fun getChatRoomsOfUser(brukerUid: String): Task<QuerySnapshot> {
        return firestore
            .collection(FirestoreKonstanter.CHATROM_COLLECTION)
            .whereArrayContains(FirestoreKonstanter.CHATROM_BRUKERE, brukerUid)
            .get()
    }
    //Finner alle meldings objecter som tilhører et chatterom
    fun getMessagesOfChatRoom(chatRoomUid: String): Task<QuerySnapshot> {
        return firestore
            .collection(FirestoreKonstanter.CHATROM_COLLECTION)
            .document(chatRoomUid)
            .collection(FirestoreKonstanter.CHATROM_MELDINGER)
                //Kanskje ikke denne funker
            .orderBy(FirestoreKonstanter.CHATROM_SISTE_MELDING_TID)
            .get()
    }

    fun createChat(brukerUid1: String, brukerUid2: String): Task<Void> {
        val chatRoom = ChatteRom(
            chatUid = generateChatUid(brukerUid1, brukerUid2),
            chatRomNavn = "Privat Chat",
            chatRomBrukere = listOf(brukerUid1, brukerUid1),
        )
        //Legger det til i firestore
        return firestore
            .collection(FirestoreKonstanter.CHATROM_COLLECTION)
            .document(chatRoom.chatUid)
            .set(chatRoom)
    }

  fun  addMessagesToChatRoom(chatRoomUid: String, melding: Melding): Task<MutableList<Task<*>>>? {
        // Setter meldingen inn i sub collection for meldinger
        val messageAdderTask = firestore
            .collection(FirestoreKonstanter.CHATROM_COLLECTION)
            .document(chatRoomUid)
            .collection(FirestoreKonstanter.CHATROM_MELDINGER)
            .document(melding.meldingUid)
            .set(melding)

        val newTimeText = hashMapOf<String, Any>(
            FirestoreKonstanter.CHATROM_SISTE_MELDING_TID to melding.meldingTid,
            FirestoreKonstanter.CHATROM_SISTE_MELDING_TEXT to melding.meldingText
        )
      // Siste melding med tid og text blir oppdatert
        val chatRoomUpdaterTask = firestore
            .collection(FirestoreKonstanter.CHATROM_COLLECTION)
            .document(chatRoomUid)
            .update(newTimeText)
        return Tasks.whenAllSuccess(messageAdderTask, chatRoomUpdaterTask)

    }

    //Oppretter et chatterom med bruker UID til de to brukerne
    fun generateChatUid(brukerUid1: String,brukerUid2: String): String {
        return if (brukerUid1 < brukerUid2) {
            brukerUid1 + brukerUid2
        } else {
            brukerUid2 + brukerUid1
        }
    }

}