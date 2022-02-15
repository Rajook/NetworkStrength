package com.example.networkstrength.utilities

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

class DataStoreManager(val context: Context)  {

    private val GENIXIAN_DATASTORE: String = "GENIXIAN_DATASTORE"
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = GENIXIAN_DATASTORE)

    companion object {

        val NAME = stringPreferencesKey("NAME")
        val ID = intPreferencesKey("ID")

    }

    suspend fun savetoDataStore(company: Company) {
        context.dataStore.edit {
            it[NAME] = company.name
            it[ID] = company.id

        }
    }

    suspend fun getFromDataStore() = context.dataStore.data.map {
        Company(
            name = it[NAME]?:"",
            id = it[ID]?:0
        )
    }

}