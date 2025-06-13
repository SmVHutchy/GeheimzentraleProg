package de.thnuernberg.bme.geheimzentrale.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RectangleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import de.thnuernberg.bme.geheimzentrale.data.model.Episode
import de.thnuernberg.bme.geheimzentrale.data.model.Kapitel
import de.thnuernberg.bme.geheimzentrale.data.model.Sprechrolle
import de.thnuernberg.bme.geheimzentrale.ui.components.GlassBox

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EpisodenDetailScreen(
    episode: Episode,
    onNavigateBack: () -> Unit
) {
    val uriHandler = LocalUriHandler.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Folge ${episode.nummer}") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Zurück")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                SubcomposeAsyncImage(
                    model = episode.coverUrl,
                    contentDescription = "Cover",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    contentScale = ContentScale.Fit
                )
            }

            item {
                Text(
                    text = episode.titel,
                    style = MaterialTheme.typography.headlineMedium
                )
            }

            item {
                Text(
                    text = "von ${episode.autor}",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            item {
                Text(
                    text = episode.beschreibung,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            if (episode.kapitel.isNotEmpty()) {
                item {
                    Text(
                        text = "Kapitel",
                        style = MaterialTheme.typography.titleLarge
                    )
                }

                items(episode.kapitel) { kapitel ->
                    ListItem(
                        headlineContent = { Text(kapitel.titel) },
                        supportingContent = {
                            Text("${kapitel.start / 1000 / 60}:${String.format("%02d", (kapitel.start / 1000) % 60)}")
                        }
                    )
                }
            }

            if (episode.sprechrollen.isNotEmpty()) {
                item {
                    Text(
                        text = "Sprecher",
                        style = MaterialTheme.typography.titleLarge
                    )
                }

                items(episode.sprechrollen) { rolle ->
                    ListItem(
                        headlineContent = { Text(rolle.rolle) },
                        supportingContent = { Text(rolle.sprecher) }
                    )
                }
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Hören auf:",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    episode.links.spotify?.let { url ->
                        Button(
                            onClick = { uriHandler.openUri(url) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RectangleShape
                        ) {
                            Icon(Icons.Default.PlayArrow, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Spotify")
                        }
                    }
                    
                    episode.links.appleMusic?.let { url ->
                        Button(
                            onClick = { uriHandler.openUri(url) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RectangleShape
                        ) {
                            Icon(Icons.Default.PlayArrow, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Apple Music")
                        }
                    }
                    
                    episode.links.amazonMusic?.let { url ->
                        Button(
                            onClick = { uriHandler.openUri(url) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RectangleShape
                        ) {
                            Icon(Icons.Default.PlayArrow, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Amazon Music")
                        }
                    }
                    
                    episode.links.youTubeMusic?.let { url ->
                        Button(
                            onClick = { uriHandler.openUri(url) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RectangleShape
                        ) {
                            Icon(Icons.Default.PlayArrow, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("YouTube Music")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun KapitelItem(kapitel: Kapitel) {
    GlassBox(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = kapitel.titel,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "${formatTime(kapitel.start)} - ${formatTime(kapitel.end)}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun SprechrolleItem(rolle: Sprechrolle) {
    GlassBox(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = rolle.rolle,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = rolle.sprecher,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            if (rolle.pseudonym != null) {
                Text(
                    text = "als ${rolle.pseudonym}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

private fun formatTime(milliseconds: Int): String {
    val seconds = milliseconds / 1000
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return "%02d:%02d".format(minutes, remainingSeconds)
} 
