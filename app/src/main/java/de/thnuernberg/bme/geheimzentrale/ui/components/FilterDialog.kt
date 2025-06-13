package de.thnuernberg.bme.geheimzentrale.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.thnuernberg.bme.geheimzentrale.data.model.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterDialog(
    currentFilter: Filter,
    onFilterChange: (Filter) -> Unit,
    onDismiss: () -> Unit
) {
    var searchText by remember { mutableStateOf(currentFilter.searchText) }
    var autor by remember { mutableStateOf(currentFilter.autor) }
    var sprecher by remember { mutableStateOf(currentFilter.sprecher) }
    var onlyListened by remember { mutableStateOf(currentFilter.onlyListened) }
    var onlyInProgress by remember { mutableStateOf(currentFilter.onlyInProgress) }
    var sortBy by remember { mutableStateOf(currentFilter.sortBy) }
    var sortDirection by remember { mutableStateOf(currentFilter.sortDirection) }
    var filterCombination by remember { mutableStateOf(currentFilter.filterCombination) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Filter") },
        text = {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Suchfeld
                OutlinedTextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    label = { Text("Suche") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Autor
                OutlinedTextField(
                    value = autor,
                    onValueChange = { autor = it },
                    label = { Text("Autor") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Sprecher
                OutlinedTextField(
                    value = sprecher,
                    onValueChange = { sprecher = it },
                    label = { Text("Sprecher") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Filter-Chips
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = onlyListened,
                        onClick = { onlyListened = !onlyListened },
                        label = { Text("Gehört") }
                    )
                    FilterChip(
                        selected = onlyInProgress,
                        onClick = { onlyInProgress = !onlyInProgress },
                        label = { Text("In Bearbeitung") }
                    )
                }

                // Sortierung
                Column {
                    Text("Sortierung", style = MaterialTheme.typography.titleSmall)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SortOption.values().forEach { option ->
                            FilterChip(
                                selected = sortBy == option,
                                onClick = { sortBy = option },
                                label = { Text(option.name) }
                            )
                        }
                    }
                }

                // Sortierrichtung
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SortDirection.values().forEach { direction ->
                        FilterChip(
                            selected = sortDirection == direction,
                            onClick = { sortDirection = direction },
                            label = { Text(direction.name) }
                        )
                    }
                }

                // Filter-Kombination
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterCombination.values().forEach { combination ->
                        FilterChip(
                            selected = filterCombination == combination,
                            onClick = { filterCombination = combination },
                            label = { Text(combination.name) }
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onFilterChange(
                        currentFilter.copy(
                            searchText = searchText,
                            autor = autor,
                            sprecher = sprecher,
                            onlyListened = onlyListened,
                            onlyInProgress = onlyInProgress,
                            sortBy = sortBy,
                            sortDirection = sortDirection,
                            filterCombination = filterCombination
                        )
                    )
                    onDismiss()
                }
            ) {
                Text("Anwenden")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Abbrechen")
            }
        }
    )
} 
