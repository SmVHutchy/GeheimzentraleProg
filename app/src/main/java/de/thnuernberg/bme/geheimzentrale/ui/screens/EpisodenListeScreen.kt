package de.thnuernberg.bme.geheimzentrale.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import de.thnuernberg.bme.geheimzentrale.data.model.Episode
import de.thnuernberg.bme.geheimzentrale.ui.components.EpisodeCard
import de.thnuernberg.bme.geheimzentrale.ui.components.FilterDialog
import de.thnuernberg.bme.geheimzentrale.ui.viewmodel.DreiFragezeichenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EpisodenListeScreen(
    viewModel: DreiFragezeichenViewModel,
    onEpisodeClick: (Episode) -> Unit
) {
    val episoden by viewModel.episoden.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Die drei ???") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(episoden) { episode ->
                EpisodeCard(
                    episode = episode,
                    onEpisodeClick = onEpisodeClick
                )
            }
        }
    }
} 