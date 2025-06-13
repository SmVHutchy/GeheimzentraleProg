package de.thnuernberg.bme.geheimzentrale.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlists")
data class Playlist(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val description: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "playlist_episodes",
    primaryKeys = ["playlistId", "episodeId"]
)
data class PlaylistEpisode(
    val playlistId: Long,
    val episodeId: Int,
    val position: Int,
    val addedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "episode_status")
data class EpisodeStatus(
    @PrimaryKey
    val episodeId: Int,
    val isListened: Boolean = false,
    val isInProgress: Boolean = false,
    val progress: Int = 0,
    val tags: List<String> = emptyList(),
    val lastUpdated: Long = System.currentTimeMillis()
) 