package de.thnuernberg.bme.geheimzentrale.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import de.thnuernberg.bme.geheimzentrale.data.model.Episode
import de.thnuernberg.bme.geheimzentrale.data.model.EpisodeStatus
import de.thnuernberg.bme.geheimzentrale.data.model.Playlist

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EpisodeCard(
    episode: Episode,
    episodeStatus: EpisodeStatus? = null,
    playlists: List<Playlist> = emptyList(),
    onEpisodeClick: (Episode) -> Unit,
    onAddToPlaylist: (Int, Long) -> Unit = { _, _ -> },
    onMarkAsListened: (Int) -> Unit = { _ -> },
    onAddTag: (Int, String) -> Unit = { _, _ -> },
    onToggleFavorite: (Int) -> Unit = { _ -> },
    modifier: Modifier = Modifier
) {
    var showPlaylistMenu by remember { mutableStateOf(false) }
    var showTagDialog by remember { mutableStateOf(false) }
    var isHovered by remember { mutableStateOf(false) }

    Card(
        onClick = { onEpisodeClick(episode) },
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize()
            .scale(if (isHovered) 1.02f else 1f),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Cover
            AsyncImage(
                model = episode.coverUrl,
                contentDescription = "Cover von ${episode.titel}",
                modifier = Modifier
                    .size(80.dp),
                contentScale = ContentScale.Crop
            )

            // Info
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = episode.titel,
                    style = MaterialTheme.typography.titleMedium
                )
                
                Text(
                    text = "Folge ${episode.nummer}",
                    style = MaterialTheme.typography.bodyMedium
                )
                
                Text(
                    text = episode.autor,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }

    // Aktions-Buttons
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        horizontalArrangement = Arrangement.End
    ) {
        // Favoriten-Button
        IconButton(onClick = { onToggleFavorite(episode.nummer) }) {
            Icon(
                imageVector = if (episodeStatus?.isFavorite == true) Icons.Default.Star else Icons.Default.StarOutline,
                contentDescription = if (episodeStatus?.isFavorite == true) "Favorit entfernen" else "Als Favorit markieren"
            )
        }

        // Playlist-Menü
        IconButton(
            onClick = { showPlaylistMenu = true }
        ) {
            Icon(
                imageVector = Icons.Default.PlaylistAdd,
                contentDescription = "Zur Playlist hinzufügen"
            )
        }

        // Tag-Button
        IconButton(
            onClick = { showTagDialog = true }
        ) {
            Icon(
                imageVector = Icons.Default.LocalOffer,
                contentDescription = "Tags verwalten"
            )
        }
    }

    // Playlist-Auswahlmenü
    if (showPlaylistMenu) {
        AlertDialog(
            onDismissRequest = { showPlaylistMenu = false },
            title = { Text("Zur Playlist hinzufügen") },
            text = {
                Column {
                    playlists.forEach { playlist ->
                        ListItem(
                            headlineContent = { Text(playlist.name) },
                            supportingContent = { Text(playlist.description ?: "") },
                            modifier = Modifier.clickable {
                                onAddToPlaylist(episode.nummer, playlist.id)
                                showPlaylistMenu = false
                            }
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showPlaylistMenu = false }) {
                    Text("Abbrechen")
                }
            }
        )
    }

    // Tag-Dialog
    if (showTagDialog) {
        var newTag by remember { mutableStateOf("") }
        
        AlertDialog(
            onDismissRequest = { showTagDialog = false },
            title = { Text("Tags verwalten") },
            text = {
                Column {
                    // Bestehende Tags anzeigen
                    episodeStatus?.tags?.forEach { tag ->
                        FilterChip(
                            selected = false,
                            onClick = { },
                            label = { Text(tag) },
                            trailingIcon = {
                                IconButton(
                                    onClick = {
                                        val updatedTags = episodeStatus.tags.toMutableList()
                                        updatedTags.remove(tag)
                                        onAddTag(episode.nummer, updatedTags.joinToString(","))
                                    }
                                ) {
                                    Icon(Icons.Default.Close, contentDescription = "Tag entfernen")
                                }
                            }
                        )
                    }

                    // Neuen Tag hinzufügen
                    OutlinedTextField(
                        value = newTag,
                        onValueChange = { newTag = it },
                        label = { Text("Neuer Tag") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (newTag.isNotBlank()) {
                            val currentTags = episodeStatus?.tags ?: emptyList()
                            val updatedTags = currentTags + newTag
                            onAddTag(episode.nummer, updatedTags.joinToString(","))
                            newTag = ""
                        }
                    }
                ) {
                    Text("Hinzufügen")
                }
            },
            dismissButton = {
                TextButton(onClick = { showTagDialog = false }) {
                    Text("Fertig")
                }
            }
        )
    }
} 