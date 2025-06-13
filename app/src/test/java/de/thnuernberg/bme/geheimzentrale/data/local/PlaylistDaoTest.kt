package de.thnuernberg.bme.geheimzentrale.data.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class PlaylistDaoTest {
    private lateinit var db: AppDatabase
    private lateinit var dao: PlaylistDao

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = db.playlistDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun tagsRoundTrip() = runBlocking {
        val status = EpisodeStatus(episodeId = 1, tags = listOf("a", "b"))
        dao.updateEpisodeStatus(status)
        val loaded = dao.getEpisodeStatus(1)
        assertEquals(listOf("a", "b"), loaded?.tags)
    }

    @Test
    fun emptyTagsRoundTrip() = runBlocking {
        val status = EpisodeStatus(episodeId = 2)
        dao.updateEpisodeStatus(status)
        val loaded = dao.getEpisodeStatus(2)
        assertEquals(emptyList<String>(), loaded?.tags)
    }
}
