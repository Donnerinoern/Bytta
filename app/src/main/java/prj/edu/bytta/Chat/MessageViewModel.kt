package prj.edu.bytta.Chat

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import prj.edu.bytta.MeldingKonstanter
import prj.edu.bytta.MeldingKonstanter.TAG
import java.lang.IllegalArgumentException



class MessageViewModel : ViewModel() {
    init {
        mottaMelding()
        //runAll()
    }

    private val melding = MutableLiveData("")
    val message: LiveData<String> = melding


    private var meldinger = MutableLiveData(emptyList<Map<String, Any>>().toMutableList())
    val msg: LiveData<MutableList<Map<String, Any>>> = meldinger

    // Oppdaterer meldingen mens brukern skriver
    fun updateMelding(message: String) {
        melding.value = message
    }

    // Send melding
    fun sendMelding() {
        val message: String = melding.value ?: throw IllegalArgumentException("Tom melding :(")
        if (message.isNotEmpty()) {
            Firebase.firestore.collection(MeldingKonstanter.MELDINGER).document().set(
                hashMapOf(
                    MeldingKonstanter.MELDINGER to message,
                    MeldingKonstanter.SENDT_AV to Firebase.auth.currentUser?.uid,
                    MeldingKonstanter.SENDT to System.currentTimeMillis()
                )
            ).addOnSuccessListener {
                melding.value = ""
            }
        }
    }

    //Motta meldinger
     private fun mottaMelding() {
        Firebase.firestore.collection(MeldingKonstanter.MELDINGER)
            .orderBy(MeldingKonstanter.SENDT)
            .addSnapshotListener { value, e ->
                if (e != null) {
                    Log.w(MeldingKonstanter.TAG, "Listener feilet", e)
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
                //Log.d(TAG, "Melding Skrevet: $list")
            }
    }

    // Oppdaterer listen med detaljer fra firestore
    private fun updateMeldinger(list: MutableList<Map<String, Any>>) {
        meldinger.value = list.asReversed()
        Log.d(TAG, "Melding: ${meldinger.value}")
    }
}