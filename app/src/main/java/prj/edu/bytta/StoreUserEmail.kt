package prj.edu.bytta

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class StoreUserEmail(private val context: Context) {

    // to make sure there's only one instance
    companion object {
        private val Context.dataStoree: DataStore<Preferences> by preferencesDataStore("userEmail")
        val USER_EMAIL_KEY = stringPreferencesKey("user_email")
    }

    //get the saved email
    val getEmail: Flow<String?> = context.dataStoree.data
        .map { preferences ->
            preferences[USER_EMAIL_KEY] ?: "FirstLast@gmail.com"
        }

    //save email into datastore
    suspend fun saveEmail(name: String) {
        context.dataStoree.edit { preferences ->
            preferences[USER_EMAIL_KEY] = name
        }
    }


}