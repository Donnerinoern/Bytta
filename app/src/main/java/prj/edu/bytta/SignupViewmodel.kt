package prj.edu.bytta

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.firebase.ui.auth.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import prj.edu.bytta.data.Event
import prj.edu.bytta.data.UserData
import java.util.UUID
import javax.inject.Inject

const val USERS = "users"

class SignupViewmodel(
    var auth: FirebaseAuth,
    val db: FirebaseFirestore,
    val storage: FirebaseStorage
)  : ViewModel() {




    val signedIn = mutableStateOf(false)
    val inProgress = mutableStateOf(false)
    val userData = mutableStateOf<UserData?>(null)
    val popupNotification = mutableStateOf <Event<String>?>(null)

    init {
        val currentUser = auth.currentUser
        signedIn.value = currentUser != null
        currentUser?.uid?.let{ uid ->
            getUserData(uid)
        }
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
        val uid = auth.currentUser?.uid
        val userData = UserData(
            userID = uid,
            username = username ?: userData.value?.username,
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

    fun signOut() {
        Firebase.auth.signOut()
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