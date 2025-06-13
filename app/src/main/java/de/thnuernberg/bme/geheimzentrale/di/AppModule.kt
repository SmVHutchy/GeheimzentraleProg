package de.thnuernberg.bme.geheimzentrale.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import de.thnuernberg.bme.geheimzentrale.data.api.DreiFragezeichenApi
import de.thnuernberg.bme.geheimzentrale.data.model.LocalDateAdapter
import java.time.LocalDate
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    
    @Provides
    @Singleton
    fun provideGson(): Gson =
        GsonBuilder()
            .registerTypeAdapter(LocalDate::class.java, LocalDateAdapter())
            .create()

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
} 