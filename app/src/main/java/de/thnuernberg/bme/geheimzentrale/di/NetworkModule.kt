package de.thnuernberg.bme.geheimzentrale.di

import android.content.Context
import com.google.gson.Gson
import de.thnuernberg.bme.geheimzentrale.data.api.DreiFragezeichenApi
import de.thnuernberg.bme.geheimzentrale.data.model.ApiResponse
import de.thnuernberg.bme.geheimzentrale.data.model.Episode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalJsonReader @Inject constructor(
    private val context: Context,
    private val gson: Gson
) {
    suspend fun readEpisoden(): List<Episode> = withContext(Dispatchers.IO) {
        try {
            val jsonString = context.assets.open("episodes.json").bufferedReader().use { it.readText() }
            val response: ApiResponse = gson.fromJson(jsonString, ApiResponse::class.java)
            response.serie
        } catch (e: IOException) {
            emptyList()
        }
    }
}

class LocalDreiFragezeichenApi @Inject constructor(
    private val jsonReader: LocalJsonReader
) : DreiFragezeichenApi {
    override suspend fun getEpisoden(): List<Episode> = jsonReader.readEpisoden()
} 