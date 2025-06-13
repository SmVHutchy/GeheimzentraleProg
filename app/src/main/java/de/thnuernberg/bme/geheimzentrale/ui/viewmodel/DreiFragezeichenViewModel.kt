package de.thnuernberg.bme.geheimzentrale.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.thnuernberg.bme.geheimzentrale.data.model.*
import de.thnuernberg.bme.geheimzentrale.data.repository.DreiFragezeichenRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DreiFragezeichenViewModel @Inject constructor(
    private val repository: DreiFragezeichenRepository
) : ViewModel() {

    private val _episoden = MutableStateFlow<List<Episode>>(emptyList())
    val episoden: StateFlow<List<Episode>> = _episoden.asStateFlow()

    private val _filter = MutableStateFlow(Filter())
    val filter: StateFlow<Filter> = _filter.asStateFlow()

    val gefilterteEpisoden: StateFlow<List<Episode>> = combine(
        episoden,
        filter
    ) { episoden, filter ->
        repository.filterEpisoden(filter).first()
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // Playlist State
    private val _playlists = MutableStateFlow<List<Playlist>>(emptyList())
    val playlists: StateFlow<List<Playlist>> = _playlists.asStateFlow()

    private val _currentPlaylist = MutableStateFlow<Playlist?>(null)
    val currentPlaylist: StateFlow<Playlist?> = _currentPlaylist.asStateFlow()

    private val _playlistEpisodes = MutableStateFlow<List<PlaylistEpisode>>(emptyList())
    val playlistEpisodes: StateFlow<List<PlaylistEpisode>> = _playlistEpisodes.asStateFlow()

    // Episode Status State
    private val _episodeStatuses = MutableStateFlow<Map<Int, EpisodeStatus>>(emptyMap())
    val episodeStatuses: StateFlow<Map<Int, EpisodeStatus>> = _episodeStatuses.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getEpisoden()
                .collect { episoden ->
                    _episoden.value = episoden
                }
        }

        viewModelScope.launch {
            repository.getAllPlaylists().collect { playlists ->
                _playlists.value = playlists
            }
        }
    }

    // Playlist Operationen
    fun createPlaylist(name: String, description: String? = null) {
        viewModelScope.launch {
            repository.createPlaylist(name, description)
        }
    }

    fun selectPlaylist(playlist: Playlist) {
        _currentPlaylist.value = playlist
        viewModelScope.launch {
            repository.getPlaylistEpisodes(playlist.id).collect { episodes ->
                _playlistEpisodes.value = episodes
            }
        }
    }

    fun addEpisodeToPlaylist(episode: Episode) {
        viewModelScope.launch {
            _currentPlaylist.value?.let { playlist ->
                repository.addEpisodeToPlaylist(playlist.id, episode.nummer)
            }
        }
    }

    fun removeEpisodeFromPlaylist(playlistEpisode: PlaylistEpisode) {
        viewModelScope.launch {
            repository.removeEpisodeFromPlaylist(playlistEpisode)
        }
    }

    // Episode Status Operationen
    fun markEpisodeAsListened(episodeId: Int) {
        viewModelScope.launch {
            repository.markEpisodeAsListened(episodeId)
        }
    }

    fun updateEpisodeProgress(episodeId: Int, progress: Int) {
        viewModelScope.launch {
            repository.updateEpisodeProgress(episodeId, progress)
        }
    }

    fun toggleFavorite(episodeId: Int) {
        viewModelScope.launch {
            val current = _episodeStatuses.value[episodeId]?.isFavorite ?: false
            repository.setEpisodeFavorite(episodeId, !current)
        }
    }

    fun getEpisodeStatus(episodeId: Int): EpisodeStatus? {
        return _episodeStatuses.value[episodeId]
    }

    // Status Updates
    init {
        viewModelScope.launch {
            repository.getListenedEpisodes().collect { statuses ->
                val newMap = _episodeStatuses.value.toMutableMap()
                statuses.forEach { status ->
                    newMap[status.episodeId] = status
                }
                _episodeStatuses.value = newMap
            }
        }

        viewModelScope.launch {
            repository.getInProgressEpisodes().collect { statuses ->
                val newMap = _episodeStatuses.value.toMutableMap()
                statuses.forEach { status ->
                    newMap[status.episodeId] = status
                }
                _episodeStatuses.value = newMap
            }
        }

        viewModelScope.launch {
            repository.getFavoriteEpisodes().collect { statuses ->
                val newMap = _episodeStatuses.value.toMutableMap()
                statuses.forEach { status ->
                    newMap[status.episodeId] = status
                }
                _episodeStatuses.value = newMap
            }
        }
    }

    // Filter Operationen
    fun updateFilter(newFilter: Filter) {
        _filter.value = newFilter
        viewModelScope.launch {
            repository.filterEpisoden(newFilter)
                .collect { filteredEpisoden ->
                    _episoden.value = filteredEpisoden
                }
        }
    }

    fun updateSearchText(text: String) {
        _filter.value = _filter.value.copy(searchText = text)
    }

    fun updateAutor(autor: String) {
        _filter.value = _filter.value.copy(autor = autor)
    }

    fun updateJahr(jahr: Set<Int>) {
        _filter.value = _filter.value.copy(jahr = jahr)
    }

    fun updateSprecher(sprecher: String) {
        _filter.value = _filter.value.copy(sprecher = sprecher)
    }

    fun updateTags(tags: Set<String>) {
        _filter.value = _filter.value.copy(tags = tags)
    }

    fun updateOnlyListened(onlyListened: Boolean) {
        _filter.value = _filter.value.copy(onlyListened = onlyListened)
    }

    fun updateOnlyInProgress(onlyInProgress: Boolean) {
        _filter.value = _filter.value.copy(onlyInProgress = onlyInProgress)
    }

    fun updateDuration(minDuration: Int?, maxDuration: Int?) {
        _filter.value = _filter.value.copy(
            minDuration = minDuration,
            maxDuration = maxDuration
        )
    }

    fun updateSort(sortBy: SortOption, direction: SortDirection) {
        _filter.value = _filter.value.copy(
            sortBy = sortBy,
            sortDirection = direction
        )
    }

    fun updateFilterCombination(combination: FilterCombination) {
        _filter.value = _filter.value.copy(filterCombination = combination)
    }

    fun resetFilter() {
        _filter.value = Filter()
    }
} 