
package prj.edu.bytta

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.net.Uri
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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import prj.edu.bytta.data.Event
import prj.edu.bytta.data.UserData
import java.util.*


const val USERS = "users"

class LoginViewModel  : ComponentActivity() {

    private var db = Firebase.firestore
    private var storage = Firebase.storage

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

    fun getUserName(username: String) {
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
       // auth = Firebase.auth

        // [END initialize_auth]
    }

    val signedIn = mutableStateOf(false)
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

    fun onSignup(username: String, email: String, password: String){
        inProgress.value = true

        db.collection(USERS).whereEqualTo("username", username).get()
            .addOnSuccessListener { documents ->
                if(documents.size() > 0) {
                    //handleException(customMessage = "Username already exists")

                    inProgress.value = false
                } else {
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener{task ->
                            if (task.isSuccessful) {
                                signedIn.value = true
                                createOrUpdateProfile(username = username)
                            } else {
                                handleException(task.exception, "Registrering feilet!")
                            }
                            inProgress.value = false
                        }
                }
            }
            .addOnFailureListener{  }
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


    private fun handleException(exception: Exception? = null, customMessage: String = ""){
                    exception?.printStackTrace()
                    val errorMessage = exception?.localizedMessage ?: ""
                    val message = if ( customMessage.isEmpty()) errorMessage else "$customMessage: $errorMessage"
                    popupNotification.value = Event(message)
                }


    fun updateProfileData(username: String){
        createOrUpdateProfile(username)
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
    fun reload() {

    }


     fun signOut() {
         Firebase.auth.signOut()
    }



    private fun uploadImage(uri: Uri, onSuccess: (Uri) -> Unit){
        inProgress.value = true

        val storageRef = storage.reference
        val uuid = UUID.randomUUID()
        val imageRef = storageRef.child("images/$uuid")
        val uploadTask = imageRef.putFile(uri)

        uploadTask.addOnSuccessListener {
            val result = it.metadata?.reference?.downloadUrl
            result?.addOnSuccessListener(onSuccess)
        }
            .addOnFailureListener{exc ->
                handleException(exc)
                inProgress.value = false
            }
    }
    fun uploadProfileImage(uri: Uri){
        uploadImage(uri){
            createOrUpdateProfile(imageUrl = it.toString())
        }
    }

}







