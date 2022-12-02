package prj.edu.bytta.profil

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase



class ProfileViewmodel : ComponentActivity() {
    val _newName = mutableStateOf("")
    val newName: State<String> = _newName


    // Setters
    fun setNewName(newname: String) {
        _newName.value = newname
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


    fun updateUserName() {

        val user = Firebase.auth.currentUser

        val profileUpdates = userProfileChangeRequest {
            displayName = newName.value
        }
        user!!.updateProfile(profileUpdates)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    getCurrentUser()
                    Log.d(TAG, "Oppdatert profil")
                }
            }
    }


    private fun getCurrentUser(): FirebaseUser? {
        val user = Firebase.auth.currentUser
        Log.d(TAG, "user display name: ${user?.displayName}, email: ${user?.email}")
        return user
    }

}






        // [END update_email]




