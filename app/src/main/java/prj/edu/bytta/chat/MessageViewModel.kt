package prj.edu.bytta.chat

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import prj.edu.bytta.chat.MeldingKonstanter.TAG
import java.lang.IllegalArgumentException

private lateinit var db: FirebaseFirestore



class MessageViewModel : ViewModel() {
    init {
        mottaMelding()

    }


    private val melding = MutableLiveData("")
    val message: LiveData<String> = melding


    private var meldinger = MutableLiveData(emptyList<Map<String, Any>>().toMutableList())
    val msg: LiveData<MutableList<Map<String, Any>>> = meldinger

    // Oppdaterer meldingen mens brukern skriver
    fun updateMelding(message: String) {
        melding.value = message
    }

    // Sender en melding som blir opprettet i en collection "Meldinger"
    // lager en collection dersom det ikke finnes noen meldinger
    // Parameterene som blir brukt er da Meldinger, Avsender og når meldingen ble sendt
    fun sendMelding() {
        db = Firebase.firestore
        val message: String = melding.value ?: throw IllegalArgumentException("Tom melding :(")
        if (message.isNotEmpty()) {
            db.collection(MeldingKonstanter.MELDINGER).document().set(
                hashMapOf(
                    MeldingKonstanter.MELDINGER to message,
                    MeldingKonstanter.SENDT_AV to Firebase.auth.currentUser?.uid,
                    MeldingKonstanter.SENDT to System.currentTimeMillis()
                )
            ).addOnSuccessListener {
                melding.value = ""
            }
            Log.d(TAG, melding.value!!)
        }
    }

    //Henter meldinger fra databasen til appen og
    //Henter de ut som en liste fra databasen og viser de
    //Fra når sist melding sendt
     private fun mottaMelding() {
        db = Firebase.firestore
        db.collection(MeldingKonstanter.MELDINGER)
            .orderBy(MeldingKonstanter.SENDT)
            .addSnapshotListener { value, e ->
                if (e != null) {
                    Log.w(TAG, "Listener feilet", e)
                    return@addSnapshotListener
                }
                val list = emptyList<Map<String, Any>>().toMutableList()
                if (value != null) {
                    for (doc in value) {
                        val data = doc.data
                        data[MeldingKonstanter.BRUKER] =
                            Firebase.auth.currentUser?.uid.toString() == data[MeldingKonstanter.SENDT_AV].toString()
                        list.add(data)
                    }
                }
                updateMeldinger(list)
            }
    }

    // Oppdaterer listen med detaljer fra firestore
    private fun updateMeldinger(list: MutableList<Map<String, Any>>) {
        meldinger.value = list.asReversed()
        Log.d(TAG, "Melding: ${meldinger.value}")
    }
}