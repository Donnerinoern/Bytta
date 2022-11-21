package prj.edu.bytta

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class LoginViewModel : ComponentActivity() {
    private val _isLoggedIn = mutableStateOf(false)
    val isLoggedIn: State<Boolean> = _isLoggedIn
    val _error = mutableStateOf("")
    val error: State<String> = _error
    val _userEmail = mutableStateOf("")
    val userEmail: State<String> = _userEmail
    val _password = mutableStateOf("")
    val password: State<String> = _password
    // Setters
    fun setUserEmail(email: String) {
        _userEmail.value = email
    }

    fun setPassword(password: String) {
        _password.value = password
    }

    fun setError(error: String) {
        _error.value = error
    }
    init {
        _isLoggedIn.value = getCurrentUser() != null
    }


    // [START declare_auth]
    private lateinit var auth: FirebaseAuth
    // [END declare_auth]


    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // [START initialize_auth]
        // Initialize Firebase Auth
        auth = Firebase.auth
        // [END initialize_auth]
    }
    fun signupBtnLink(context: Context){
        val intent = Intent(context, Signup::class.java)
        context.startActivity(intent)

    }



    fun createUserWithEmailAndPassword() {
        Firebase.auth.createUserWithEmailAndPassword(userEmail.value, password.value)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
        // [END create_user_with_email]
    }

    fun signInWithEmailAndPassword(context: Context) {
        Firebase.auth.signInWithEmailAndPassword(userEmail.value, password.value)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val intent = Intent(context, HomeActivity::class.java)
                    context.startActivity(intent)

                } else {
                    // If sign in fails, display a message to the user.
                    _error.value = task.exception?.localizedMessage ?: "Unknown error"
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                }
            }
    }

    private fun getCurrentUser(): FirebaseUser? {
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



     fun signOut() {
         Firebase.auth.signOut()
    }
}




