package de.thnuernberg.bme.geheimzentrale.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import de.thnuernberg.bme.geheimzentrale.data.api.DreiFragezeichenApi
import de.thnuernberg.bme.geheimzentrale.data.local.PlaylistDao
import de.thnuernberg.bme.geheimzentrale.data.repository.DreiFragezeichenRepository
import de.thnuernberg.bme.geheimzentrale.data.model.EpisodeMapper
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    
    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder()
        .registerTypeAdapter(
            java.time.LocalDate::class.java,
            JsonDeserializer { json, _, _ -> java.time.LocalDate.parse(json.asString) }
        )
        .create()

    @Provides
    @Singleton
    fun provideLocalJsonReader(
        @ApplicationContext context: Context,
        gson: Gson,
        episodeMapper: EpisodeMapper
    ): LocalJsonReader = LocalJsonReader(context, gson, episodeMapper)

    @Provides
    @Singleton
    fun provideDreiFragezeichenApi(
        jsonReader: LocalJsonReader
    ): DreiFragezeichenApi = LocalDreiFragezeichenApi(jsonReader)

    @Provides
    @Singleton
    fun provideDreiFragezeichenRepository(
        playlistDao: PlaylistDao,
        api: DreiFragezeichenApi
    ): DreiFragezeichenRepository = DreiFragezeichenRepository(playlistDao, api)
}
