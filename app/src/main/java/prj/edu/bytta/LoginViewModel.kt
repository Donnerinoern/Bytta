package prj.edu.bytta

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import prj.edu.bytta.data.Event
import prj.edu.bytta.data.UserData


const val USERS = "users"

class LoginViewModel : ComponentActivity() {

    private var db = Firebase.firestore

    // [START declare_auth]
    private lateinit var auth: FirebaseAuth
    // [END declare_auth]

    private val _isLoggedIn = mutableStateOf(false)
    val isLoggedIn: State<Boolean> = _isLoggedIn
    val _error = mutableStateOf("")
    val error: State<String> = _error
    val _userName = mutableStateOf("")
    val userName: State<String> = _userName
    val _userEmail = mutableStateOf("")
    val userEmail: State<String> = _userEmail
    val _password = mutableStateOf("")
    val password: State<String> = _password


    // Setters
    fun setUserName(username: String) {
        _userName.value = username
    }

    fun setUserEmail(email: String) {
        _userEmail.value = email
    }

    fun setPassword(password: String) {
        _password.value = password
    }

    fun setError(error: String.Companion) {
        _error.value = error.toString()
    }

    init {
        _isLoggedIn.value = getCurrentUser() != null
    }


    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // [START initialize_auth]
        // Initialize Firebase Auth
        auth = Firebase.auth
        // [END initialize_auth]
    }

    val inProgress = mutableStateOf(false)
    val userData = mutableStateOf<UserData?>(null)
    val popupNotification = mutableStateOf<Event<String>?>(null)


    fun createUserWithEmailAndPassword() {
        Firebase.auth.createUserWithEmailAndPassword(userEmail.value, password.value)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    createOrUpdateProfile(username = userName.value)
                } else {
                // email already in use
                        _error.value = "Email er allerede i bruk"
                        Log.w(TAG, "createUserWithEmailAndPassword:failure", task.exception)
            }

                }
            }

    fun signInWithEmailAndPassword() {
        Firebase.auth.signInWithEmailAndPassword(userEmail.value, password.value)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithEmail:success")
                } else {
                    // If sign in fails, display a message to the user.
                    _error.value = "Feil brukernavn eller password"
                    Log.w(TAG, "signInWithEmail:failure", task.exception)

                }
            }
    }

        fun getCurrentUser(): FirebaseUser? {
        val user = Firebase.auth.currentUser
        Log.d(TAG, "user display name: ${user?.displayName}, email: ${user?.email}")
        return user
    }


    fun isValidEmailAndPassword(): Boolean {
        if (userEmail.value.isBlank() || password.value.isBlank()) {
            return false
        }
        return true
    }


    private fun createOrUpdateProfile(
        username: String? = null,
        imageUrl: String? = null
    ){
        val uid = Firebase.auth.currentUser?.uid
        val userData = UserData(
            userID = uid,
            username = username ?: userName.value,
            imageUrl = imageUrl ?: userData.value?.imageUrl
        )
        uid?.let { uid ->
            inProgress.value = true
            db.collection(USERS).document(uid).get().addOnSuccessListener {
                if (it.exists()) {
                    it.reference.update(userData.toMap())
                        .addOnSuccessListener {
                            this.userData.value = userData
                            inProgress.value = false
                        }
                        .addOnFailureListener{
                            handleException(it, "Cannot update user")
                            Log.d(ContentValues.TAG, "Bruker eksisterer, kan ikke oppdatere")
                            inProgress.value = false
                        }
                } else {
                    db.collection(USERS).document(uid).set(userData)
                    getUserData(uid)
                    inProgress.value = false
                }
            }
                .addOnFailureListener { exc ->
                    handleException(exc, "Kan ikke lage bruker")
                    Log.d(ContentValues.TAG, "Kan ikke lage bruker!")
                    inProgress.value = false
                }
        }

    }

    private fun getUserData(uid: String) {

    }

    private fun handleException(exception: Exception? = null, customMessage: String = ""){
                    exception?.printStackTrace()
                    val errorMessage = exception?.localizedMessage ?: ""
                    val message = if ( customMessage.isEmpty()) errorMessage else "$customMessage: $errorMessage"
                    popupNotification.value = Event(message)
                }
    @Composable
    fun ErrorField() {
        Text(
            text = _error.value,
            modifier = Modifier.fillMaxWidth(),
            color = Color.Red,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null){
            reload();
        }
    }

    fun reload() {

    }


     fun signOut() {
         Firebase.auth.signOut()
    }
}




