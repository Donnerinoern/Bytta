package prj.edu.bytta

import android.content.ContentValues.TAG
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class profileViewmodel : ComponentActivity() {

    private fun updateEmail() {
        // [START update_email]
        val user = Firebase.auth.currentUser

        user!!.updateEmail("")
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "User email address updated.")
                }
            }
        // [END update_email]
    }



}