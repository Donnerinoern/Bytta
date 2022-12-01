package prj.edu.bytta.meldinger

import android.content.Context
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import dagger.hilt.android.qualifiers.ApplicationContext
import prj.edu.bytta.BYTTA_CHAT_PREF
import prj.edu.bytta.Bruker
import prj.edu.bytta.FirestoreKonstanter
import prj.edu.bytta.MeldingKonstanter.TAG
import java.util.*
import javax.annotation.Nullable
import javax.inject.Inject


class BrukerRepository  @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth,
    @ApplicationContext private val context: Context

){

    //Henter brukeren som er innlogget
    fun getCurrentUser(): Task<QuerySnapshot> {
        val uid = firebaseAuth.currentUser!!.uid
        return firestore
            .collection(FirestoreKonstanter.BRUKER_COLLECTION)
            .whereEqualTo(FirestoreKonstanter.BRUKER_UID, uid)
            .get()
    }



   @Nullable
   fun addUserContact(bruker: Bruker, contactUserUid: String): Task<Void>? {
    if (bruker.kontakter.contains(contactUserUid)) {
        Log.d(TAG, "Brukern har allerede denne brukeren i Chats")
        return null
    }
       val updateContacts = bruker.kontakter + contactUserUid
       return firestore
           .collection(FirestoreKonstanter.BRUKER_COLLECTION)
           .document(bruker.uid)
           .update(FirestoreKonstanter.BRUKER_KONTAKTER, updateContacts)
   }

    fun getUsers(): Task<QuerySnapshot> {
        return firestore
            .collection(FirestoreKonstanter.BRUKER_COLLECTION)
            .get()
    }

    fun getUserByUid(brukerUidList: List<String>): Task<QuerySnapshot> {
        return firestore
            .collection(FirestoreKonstanter.BRUKER_COLLECTION)
            .whereIn(FirestoreKonstanter.BRUKER_UID,brukerUidList)
            .get()
    }

    fun getUserById(brukerUid: String): Task<DocumentSnapshot>{
        return firestore
            .collection(FirestoreKonstanter.BRUKER_COLLECTION)
            .document(brukerUid)
            .get()

    }

    fun createUid():  String {
        return UUID.randomUUID().toString().replace("-","")
    }

    fun updateBrukerMeldingToken(brukerUid: String): Task<Void> {
    val prefs = context.getSharedPreferences(BYTTA_CHAT_PREF, Context.MODE_PRIVATE)
        val deviceMeldingToken = prefs.getString(MELDING_TOKEN, TOKEN_IKKE_FUNNET) ?: TOKEN_IKKE_FUNNET
        Log.d(TAG, "Fuck tokens, fra $deviceMeldingToken")

        return firestore
            .collection(FirestoreKonstanter.BRUKER_COLLECTION)
            .document(brukerUid)
            .update(FirestoreKonstanter.BRUKER_MELDING_TOKEN, deviceMeldingToken)

    }


}