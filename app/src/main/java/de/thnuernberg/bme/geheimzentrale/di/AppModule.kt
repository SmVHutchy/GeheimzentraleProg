package de.thnuernberg.bme.geheimzentrale.di

import android.content.Context
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import de.thnuernberg.bme.geheimzentrale.data.api.DreiFragezeichenApi
import de.thnuernberg.bme.geheimzentrale.data.local.PlaylistDao
import de.thnuernberg.bme.geheimzentrale.data.repository.DreiFragezeichenRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    
    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()

    @Provides
    @Singleton
    fun provideLocalJsonReader(
        @ApplicationContext context: Context,
        gson: Gson
    ): LocalJsonReader = LocalJsonReader(context, gson)

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
