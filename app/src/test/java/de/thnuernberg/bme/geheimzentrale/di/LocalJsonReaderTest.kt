package de.thnuernberg.bme.geheimzentrale.di

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import de.thnuernberg.bme.geheimzentrale.data.model.EpisodeMapper
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class LocalJsonReaderTest {
    @Test
    fun readEpisodeFromJson() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val gson = AppModule.provideGson()
        val mapper = EpisodeMapper()
        val reader = LocalJsonReader(context, gson, mapper)

        val episodes = reader.readEpisoden()
        assertEquals(1, episodes.size)
        val episode = episodes.first()

        assertEquals(1, episode.nummer)
        assertEquals("Die Probe", episode.titel)
        assertEquals("Testautor", episode.autor)
        assertEquals(listOf("S1", "S2"), episode.sprecher)
        assertEquals(3, episode.dauer)
        assertEquals("cover.png", episode.coverUrl)
    }
}
