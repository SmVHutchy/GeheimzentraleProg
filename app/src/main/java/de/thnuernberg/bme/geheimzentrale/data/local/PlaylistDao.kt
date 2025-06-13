package de.thnuernberg.bme.geheimzentrale.data.local

import androidx.room.*
import de.thnuernberg.bme.geheimzentrale.data.model.Playlist
import de.thnuernberg.bme.geheimzentrale.data.model.PlaylistEpisode
import de.thnuernberg.bme.geheimzentrale.data.model.EpisodeStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {
    @Query("SELECT * FROM playlists ORDER BY createdAt DESC")
    fun getAllPlaylists(): Flow<List<Playlist>>

    @Query("SELECT * FROM playlists WHERE id = :playlistId")
    suspend fun getPlaylist(playlistId: Long): Playlist?

    @Insert
    suspend fun createPlaylist(playlist: Playlist): Long

    @Update
    suspend fun updatePlaylist(playlist: Playlist)

    @Delete
    suspend fun deletePlaylist(playlist: Playlist)

    @Query("SELECT * FROM playlist_episodes WHERE playlistId = :playlistId ORDER BY position ASC")
    fun getPlaylistEpisodes(playlistId: Long): Flow<List<PlaylistEpisode>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addEpisodeToPlaylist(playlistEpisode: PlaylistEpisode)

    @Delete
    suspend fun removeEpisodeFromPlaylist(playlistEpisode: PlaylistEpisode)

    @Query("SELECT MAX(position) FROM playlist_episodes WHERE playlistId = :playlistId")
    suspend fun getMaxPosition(playlistId: Long): Int?

    @Query("SELECT * FROM episode_status WHERE episodeId = :episodeId")
    suspend fun getEpisodeStatus(episodeId: Int): EpisodeStatus?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateEpisodeStatus(episodeStatus: EpisodeStatus)

    @Query("SELECT * FROM episode_status WHERE isListened = 1")
    fun getListenedEpisodes(): Flow<List<EpisodeStatus>>

    @Query("SELECT * FROM episode_status WHERE isInProgress = 1")
    fun getInProgressEpisodes(): Flow<List<EpisodeStatus>>

    @Query("SELECT * FROM episode_status WHERE tags LIKE '%' || :tag || '%'")
    fun getEpisodesByTag(tag: String): Flow<List<EpisodeStatus>>

    @Query("UPDATE episode_status SET tags = :tags WHERE episodeId = :episodeId")
    suspend fun updateEpisodeTags(episodeId: Int, tags: String)
} 