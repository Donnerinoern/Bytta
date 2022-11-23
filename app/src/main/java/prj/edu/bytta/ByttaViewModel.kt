package prj.edu.bytta

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.toObject
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

//const val USERS = "users"
const val TRADES = "trades"

@HiltViewModel
class ByttaViewModel @Inject constructor(
    val auth: FirebaseAuth,
    val db: FirebaseFirestore
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
        documents.forEach { doc ->
            val trade = doc.toObject<TradeData>()
            newTrades.add(trade)
        }
        outState.value = newTrades
    }
}