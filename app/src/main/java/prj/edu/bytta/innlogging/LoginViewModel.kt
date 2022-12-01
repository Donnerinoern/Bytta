package prj.edu.bytta.innlogging

import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import prj.edu.bytta.HomeActivity
import prj.edu.bytta.data.Event
import prj.edu.bytta.data.UserData
import java.util.*

const val USERS = "users"

class LoginViewModel : ComponentActivity() {

    private var db = Firebase.firestore
    private lateinit var storage: FirebaseStorage
    private lateinit var auth: FirebaseAuth

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


    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth

    }

    val inProgress = mutableStateOf(false)
    val userData = mutableStateOf<UserData?>(null)
    val popupNotification = mutableStateOf<Event<String>?>(null)


    fun createUserWithEmailAndPassword() {
        Firebase.auth.createUserWithEmailAndPassword(userEmail.value, password.value)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    updateProfile()
                } else {
                    // email already in use
                    Log.w(TAG, "createUserWithEmailAndPassword:failure", task.exception)
                    _error.value = "Email er allerede i bruk"

                }

            }
    }

    fun signInWithEmailAndPassword() {
        Firebase.auth.signInWithEmailAndPassword(userEmail.value, password.value)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithEmail:success")
                    getCurrentUser()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    _error.value = "Feil brukernavn eller password"


                }
            }
    }

    fun getCurrentUser(): FirebaseUser? {
        val user = Firebase.auth.currentUser
        Log.d(TAG, "username: ${user?.displayName}, email: ${user?.email}")
        return user
    }


    fun isValidEmailAndPassword(): Boolean {
        if (userEmail.value.isBlank() || password.value.isBlank()) {
            return false
        }
        return true
    }


  fun updateProfile() {

      val user = Firebase.auth.currentUser

      val profileUpdates = userProfileChangeRequest {
          displayName = userName.value
          photoUri = Uri.parse("")
      }
      user!!.updateProfile(profileUpdates)
          .addOnCompleteListener { task ->
              if (task.isSuccessful) {
                  Log.d(TAG, "Oppdatert profil")
              }
          }
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


    fun reload() {

    }


     fun signOut() {
         Firebase.auth.signOut()
    }
}




