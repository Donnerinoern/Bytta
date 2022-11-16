package prj.edu.bytta

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ProfileViewmodel : ComponentActivity() {

    private val _userEmail = mutableStateOf("")
    val userEmail: State<String> = _userEmail
    // Setters
    fun setUserEmail(email: String) {
        _userEmail.value = email
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


    fun updateEmail() {
        // [START update_email]
        val user = Firebase.auth.currentUser


                user!!.updateEmail(userEmail.toString())
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d(TAG, "User email address updated.")
                        }
                    }
            }
        }
        // [END update_email]




