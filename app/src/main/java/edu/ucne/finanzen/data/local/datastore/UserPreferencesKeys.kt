package edu.ucne.finanzen.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject


val Context.userDataStore by preferencesDataStore(name = "user_prefs")

class UserDataStore @Inject constructor(
    private val context: Context
) {
    private val dataStore = context.userDataStore

    val userIdFlow: Flow<Int?> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { prefs ->
            prefs[UserPreferencesKeys.USER_ID]
        }

    suspend fun saveUserId(id: Int) {
        dataStore.edit { prefs ->
            prefs[UserPreferencesKeys.USER_ID] = id
        }
    }

    suspend fun clearUserId() {
        dataStore.edit { prefs ->
            prefs.clear()
        }
    }
}