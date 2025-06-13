package de.thnuernberg.bme.geheimzentrale.data.api

import de.thnuernberg.bme.geheimzentrale.data.model.Episode
import retrofit2.http.GET

interface DreiFragezeichenApi {
    @GET("episodes.json")
    suspend fun getEpisoden(): List<Episode>
} 