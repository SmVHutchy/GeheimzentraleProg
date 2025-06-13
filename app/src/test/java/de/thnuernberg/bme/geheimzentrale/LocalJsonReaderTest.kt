package de.thnuernberg.bme.geheimzentrale

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.google.gson.GsonBuilder
import de.thnuernberg.bme.geheimzentrale.data.model.LocalDateAdapter
import de.thnuernberg.bme.geheimzentrale.di.LocalJsonReader
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.time.LocalDate

@RunWith(RobolectricTestRunner::class)
class LocalJsonReaderTest {
    private val context: Context = ApplicationProvider.getApplicationContext()
    private val gson = GsonBuilder()
        .registerTypeAdapter(LocalDate::class.java, LocalDateAdapter())
        .create()

    @Test
    fun readEpisoden_parsesValidJson() = runBlocking {
        val reader = LocalJsonReader(context, gson)
        val episodes = reader.readEpisoden()
        assertTrue(episodes.isNotEmpty())
    }

    @Test
    fun readEpisoden_invalidJson_returnsEmptyList() = runBlocking {
        val reader = LocalJsonReader(context, gson, "invalid.json")
        val episodes = reader.readEpisoden()
        assertTrue(episodes.isEmpty())
    }
}
