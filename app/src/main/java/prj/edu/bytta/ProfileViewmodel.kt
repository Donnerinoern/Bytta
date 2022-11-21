package prj.edu.bytta

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase



class ProfileViewmodel : ComponentActivity() {
    val _password = mutableStateOf("")
    val password: State<String> = _password
    val _userEmail = mutableStateOf("")
    val userEmail: State<String> = _userEmail

    // Setters
    fun setUserEmail(email: String) {
        _userEmail.value = email
    }

    fun setPassword(password: String) {
        _password.value = password
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




    fun updateEmail(email: String) {
        // [START update_email]
        val user = Firebase.auth.currentUser


                    user!!.updateEmail(_userEmail.toString())
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d(TAG, "User email address updated.")
                            } else {
                                Log.d(TAG, "ERRRROORRR")


                            }
                        }
                    }
                }


    fun getCurrentUser(): FirebaseUser? {
        val user = Firebase.auth.currentUser
        Log.d(TAG, "user display name: ${user?.displayName}, email: ${user?.email}")
        return user
    }








        // [END update_email]




