package prj.edu.bytta

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import prj.edu.bytta.data.Event
import prj.edu.bytta.data.UserData
import prj.edu.bytta.main.popupNotification
import java.util.*
import prj.edu.bytta.chat.MeldingKonstanter.TAG
import prj.edu.bytta.innlogging.LoginViewModel
import java.util.*
import javax.inject.Inject
import kotlin.text.Typography.dagger

const val USERS = "users"
const val TRADES = "trades"

@HiltViewModel
class ByttaViewModel @Inject constructor(
    val auth: FirebaseAuth,
    val db: FirebaseFirestore,
    val storage: FirebaseStorage
) : ViewModel() {

    val signedIn = mutableStateOf(false)
    val userData = mutableStateOf<UserData?>(null)
    val inProgress = mutableStateOf(false)
    val refreshTradesProgress = mutableStateOf(false)

    val trades = mutableStateOf<List<TradeData>>(listOf())
    val tradesFeedProgress = mutableStateOf(false)

    init {
        val currentUser = auth.currentUser
        signedIn.value = currentUser != null
        currentUser?.uid?.let { uid ->
            getUserData(uid)
        }
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


    val _userName = mutableStateOf("")
    val userName: State<String> = _userName

    private fun handleException(exception: Exception? = null, customMessage: String = ""){
        exception?.printStackTrace()
        val errorMessage = exception?.localizedMessage ?: ""
        val message = if ( customMessage.isEmpty()) errorMessage else "$customMessage: $errorMessage"
        popupNotification.value = Event(message)
    }

    private fun createOrUpdateProfile(

        imageUrl: String? = null
    ) {
        val uid = auth.currentUser?.uid
        val userData = UserData(
            imageUrl = imageUrl ?: userData.value?.imageUrl
// Kode som kommuniserer med data klassen UserData, og bidrar til å oppdatere profilbilde
        )

        uid?.let { uid ->
            inProgress.value = true
            db.collection(USERS).document(uid).get()
                .addOnSuccessListener {
                    if (it.exists()) {
                        it.reference.update(userData.toMap())
                            .addOnSuccessListener {
                                this.userData.value = userData
                                inProgress.value = false
                            }
                            .addOnFailureListener {
                                handleException(it, "Cannot update user")
                                inProgress.value = false
                            }
                    } else {
                        db.collection(USERS).document(uid).set(userData)
                        getUserData(uid)
                        inProgress.value = false
                    }
                }
                .addOnFailureListener { exc ->
                    handleException(exc, "Cannot create user")
                    inProgress.value = false
                }
        }
    }

    private fun getUserData(uid: String) {
        inProgress.value = true
        db.collection(USERS).document(uid).get()
            .addOnSuccessListener {
                val user = it.toObject<UserData>()
                userData.value = user
                inProgress.value = false
            }
            .addOnFailureListener { exc ->
                handleException(exc, "Cannot retrieve user data")
                inProgress.value = false
            }
    }

    private fun uploadImage(uri: Uri, onSuccess: (Uri) -> Unit) {
        inProgress.value = true

        val storageRef = storage.reference
        val uuid = UUID.randomUUID()
        val imageRef = storageRef.child("images/$uuid")

        // Laster opp bildefil til firebase storage
        val uploadTask = imageRef.putFile(uri)
        uploadTask
            .addOnSuccessListener {
                val result = it.metadata?.reference?.downloadUrl
                result?.addOnSuccessListener(onSuccess)
            }
            .addOnFailureListener { exc ->
                handleException(exc)
                inProgress.value = false
            }
    }

    fun uploadProfileImage(uri: Uri) {
        uploadImage(uri) {
            createOrUpdateProfile(imageUrl = it.toString())

        }
    }

    fun uploadTrade(uri: Uri, body: String, item: String, viewModel: LoginViewModel) {
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
        val tradeData = TradeData(body, item, viewModel.getCurrentUser()?.displayName, "https://firebasestorage.googleapis.com/v0/b/byttamob.appspot.com/o/images%2F${uuid}?alt=media")
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