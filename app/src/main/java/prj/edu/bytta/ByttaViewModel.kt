package prj.edu.bytta

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import prj.edu.bytta.MeldingKonstanter.TAG
import java.util.*
import javax.inject.Inject

//const val USERS = "users"
const val TRADES = "trades"

@HiltViewModel
class ByttaViewModel @Inject constructor(
    val auth: FirebaseAuth,
    val db: FirebaseFirestore,
    val storage: FirebaseStorage
) : ViewModel() {

    val inProgress = mutableStateOf(false)
    val refreshTradesProgress = mutableStateOf(false)

    val trades = mutableStateOf<List<TradeData>>(listOf())
    val tradesFeedProgress = mutableStateOf(false)

    init {
        val currentUser = auth.currentUser
        refreshFeed()
    }

    private fun refreshFeed() {
        tradesFeedProgress.value = true
        db.collection(TRADES)
            .get()
            .addOnSuccessListener { results ->
                convertTrades(results, trades)
                refreshTradesProgress.value = false
            }
            .addOnFailureListener { exception ->
                exception.printStackTrace()
                refreshTradesProgress.value = false
            }
    }

    private fun convertTrades(documents: QuerySnapshot, outState: MutableState<List<TradeData>>) {
        val newTrades = mutableListOf<TradeData>()
        val storageRef = storage.reference
        documents.forEach { doc ->
            val trade = doc.toObject<TradeData>()
            newTrades.add(trade)
        }
        outState.value = newTrades
    }

    fun uploadTrade(uri: Uri, body: String, item: String) {
        inProgress.value = true

        val storageRef = storage.reference
        val uuid = UUID.randomUUID()
        val imageRef = storageRef.child("images/$uuid")
        val uploadTask = imageRef.putFile(uri)

        uploadTask.addOnSuccessListener {
            val result = it.metadata?.reference?.downloadUrl
            result?.addOnSuccessListener(){}
        }
            .addOnFailureListener{exc ->
                inProgress.value = false
            }
        val tradeData = TradeData(body, item, getCurrentUser()?.displayName, "https://firebasestorage.googleapis.com/v0/b/byttamob.appspot.com/o/images%2F${uuid}?alt=media")
        db.collection("trades")
            .add(tradeData)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }

    }
}