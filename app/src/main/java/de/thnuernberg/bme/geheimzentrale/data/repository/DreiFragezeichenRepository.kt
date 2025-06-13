package de.thnuernberg.bme.geheimzentrale.data.repository

import de.thnuernberg.bme.geheimzentrale.data.api.DreiFragezeichenApi
import de.thnuernberg.bme.geheimzentrale.data.local.PlaylistDao
import de.thnuernberg.bme.geheimzentrale.data.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DreiFragezeichenRepository @Inject constructor(
    private val playlistDao: PlaylistDao,
    private val api: DreiFragezeichenApi
) {
    fun getEpisoden(): Flow<List<Episode>> = flow {
        emit(api.getEpisoden())
    }

    // Playlist Funktionen
    fun getAllPlaylists(): Flow<List<Playlist>> = playlistDao.getAllPlaylists()

    suspend fun getPlaylist(playlistId: Long): Playlist? = playlistDao.getPlaylist(playlistId)

    suspend fun createPlaylist(name: String, description: String? = null): Long {
        val playlist = Playlist(name = name, description = description)
        return playlistDao.createPlaylist(playlist)
    }

    suspend fun updatePlaylist(playlist: Playlist) = playlistDao.updatePlaylist(playlist)

    suspend fun deletePlaylist(playlist: Playlist) = playlistDao.deletePlaylist(playlist)

    fun getPlaylistEpisodes(playlistId: Long): Flow<List<PlaylistEpisode>> = 
        playlistDao.getPlaylistEpisodes(playlistId)

    suspend fun addEpisodeToPlaylist(playlistId: Long, episodeId: Int) {
        val maxPosition = playlistDao.getMaxPosition(playlistId) ?: 0
        val playlistEpisode = PlaylistEpisode(
            playlistId = playlistId,
            episodeId = episodeId,
            position = maxPosition + 1
        )
        playlistDao.addEpisodeToPlaylist(playlistEpisode)
    }

    suspend fun removeEpisodeFromPlaylist(playlistEpisode: PlaylistEpisode) =
        playlistDao.removeEpisodeFromPlaylist(playlistEpisode)

    // Episode Status Funktionen
    suspend fun getEpisodeStatus(episodeId: Int): EpisodeStatus? =
        playlistDao.getEpisodeStatus(episodeId)

    suspend fun updateEpisodeStatus(episodeStatus: EpisodeStatus) =
        playlistDao.updateEpisodeStatus(episodeStatus)

    suspend fun markEpisodeAsListened(episodeId: Int) {
        val status = playlistDao.getEpisodeStatus(episodeId) ?: EpisodeStatus(episodeId = episodeId)
        playlistDao.updateEpisodeStatus(status.copy(isListened = true))
    }

    suspend fun updateEpisodeProgress(episodeId: Int, progress: Int) {
        val status = playlistDao.getEpisodeStatus(episodeId) ?: EpisodeStatus(episodeId = episodeId)
        playlistDao.updateEpisodeStatus(status.copy(
            progress = progress,
            isInProgress = true
        ))
    }

    fun getListenedEpisodes(): Flow<List<EpisodeStatus>> = playlistDao.getListenedEpisodes()

    fun getInProgressEpisodes(): Flow<List<EpisodeStatus>> = playlistDao.getInProgressEpisodes()

    fun getFavoriteEpisodes(): Flow<List<EpisodeStatus>> = playlistDao.getFavoriteEpisodes()

    suspend fun setEpisodeFavorite(episodeId: Int, isFavorite: Boolean) {
        val status = playlistDao.getEpisodeStatus(episodeId) ?: EpisodeStatus(episodeId = episodeId)
        playlistDao.updateEpisodeStatus(status.copy(isFavorite = isFavorite))
    }

    fun filterEpisoden(filter: Filter): Flow<List<Episode>> = flow {
        val episoden = getEpisoden().first()
        val filteredEpisoden = when (filter.filterCombination) {
            FilterCombination.AND -> filterEpisodenAnd(episoden, filter)
            FilterCombination.OR -> filterEpisodenOr(episoden, filter)
        }
        emit(filteredEpisoden)
    }

    private suspend fun filterEpisodenAnd(episoden: List<Episode>, filter: Filter): List<Episode> {
        return episoden.filter { episode ->
            val matchesSearch = filter.searchText.isEmpty() || 
                episode.titel.contains(filter.searchText, ignoreCase = true) ||
                episode.beschreibung.contains(filter.searchText, ignoreCase = true)
            
            val matchesAutor = filter.autor.isEmpty() || 
                episode.autor.contains(filter.autor, ignoreCase = true)
            
            val matchesJahr = filter.jahr.isEmpty() || episode.erscheinungsdatum.year in filter.jahr
            
            val matchesSprecher = filter.sprecher.isEmpty() || 
                episode.sprecher.any { it.contains(filter.sprecher, ignoreCase = true) }
            
            val matchesTags = filter.tags.isEmpty() || 
                filter.tags.all { tag -> 
                    playlistDao.getEpisodesByTag(tag).first().any { it.episodeId == episode.nummer }
                }
            
            val matchesDuration = (filter.minDuration == null || episode.dauer >= filter.minDuration) &&
                (filter.maxDuration == null || episode.dauer <= filter.maxDuration)
            
            val matchesStatus = (!filter.onlyListened || isEpisodeListened(episode.nummer)) &&
                (!filter.onlyInProgress || isEpisodeInProgress(episode.nummer))
            
            matchesSearch && matchesAutor && matchesJahr && matchesSprecher && 
                matchesTags && matchesDuration && matchesStatus
        }
    }

    private suspend fun filterEpisodenOr(episoden: List<Episode>, filter: Filter): List<Episode> {
        return episoden.filter { episode ->
            val matchesSearch = filter.searchText.isEmpty() || 
                episode.titel.contains(filter.searchText, ignoreCase = true) ||
                episode.beschreibung.contains(filter.searchText, ignoreCase = true)
            
            val matchesAutor = filter.autor.isEmpty() || 
                episode.autor.contains(filter.autor, ignoreCase = true)
            
            val matchesJahr = filter.jahr.isEmpty() || episode.erscheinungsdatum.year in filter.jahr
            
            val matchesSprecher = filter.sprecher.isEmpty() || 
                episode.sprecher.any { it.contains(filter.sprecher, ignoreCase = true) }
            
            val matchesTags = filter.tags.isEmpty() || 
                filter.tags.any { tag -> 
                    playlistDao.getEpisodesByTag(tag).first().any { it.episodeId == episode.nummer }
                }
            
            val matchesDuration = (filter.minDuration == null || episode.dauer >= filter.minDuration) ||
                (filter.maxDuration == null || episode.dauer <= filter.maxDuration)
            
            val matchesStatus = (!filter.onlyListened || isEpisodeListened(episode.nummer)) ||
                (!filter.onlyInProgress || isEpisodeInProgress(episode.nummer))
            
            matchesSearch || matchesAutor || matchesJahr || matchesSprecher || 
                matchesTags || matchesDuration || matchesStatus
        }
    }

    private suspend fun isEpisodeListened(episodeId: Int): Boolean {
        return playlistDao.getEpisodeStatus(episodeId)?.isListened ?: false
    }

    private suspend fun isEpisodeInProgress(episodeId: Int): Boolean {
        return playlistDao.getEpisodeStatus(episodeId)?.isInProgress ?: false
    }
} 
