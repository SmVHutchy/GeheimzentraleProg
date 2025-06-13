package de.thnuernberg.bme.geheimzentrale.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import de.thnuernberg.bme.geheimzentrale.ui.screens.EpisodenDetailScreen
import de.thnuernberg.bme.geheimzentrale.ui.screens.EpisodenListeScreen
import de.thnuernberg.bme.geheimzentrale.ui.viewmodel.DreiFragezeichenViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val viewModel: DreiFragezeichenViewModel = hiltViewModel()
    
    NavHost(
        navController = navController,
        startDestination = "episodenliste"
    ) {
        composable("episodenliste") {
            EpisodenListeScreen(
                viewModel = viewModel,
                onEpisodeClick = { episode ->
                    navController.navigate("episode/${episode.nummer}")
                }
            )
        }
        
        composable("episode/{nummer}") { backStackEntry ->
            val nummer = backStackEntry.arguments?.getString("nummer")?.toIntOrNull() ?: return@composable
            val episode = viewModel.episoden.value.find { it.nummer == nummer } ?: return@composable
            
            EpisodenDetailScreen(
                episode = episode,
                onNavigateBack = { navController.navigateUp() }
            )
        }
    }
} 