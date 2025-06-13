package de.thnuernberg.bme.geheimzentrale.data.model

enum class FilterCombination {
    AND, OR
}

enum class SortOption {
    NUMMER, TITEL, ERSCHEINUNGSDATUM, AUTOR, DAUER
}

enum class SortDirection {
    ASCENDING, DESCENDING
}

data class Filter(
    val searchText: String = "",
    val autor: String = "",
    val jahr: Set<Int> = emptySet(),
    val sprecher: String = "",
    val tags: Set<String> = emptySet(),
    val minDuration: Int? = null,
    val maxDuration: Int? = null,
    val onlyListened: Boolean = false,
    val onlyInProgress: Boolean = false,
    val sortBy: SortOption = SortOption.NUMMER,
    val sortDirection: SortDirection = SortDirection.ASCENDING,
    val filterCombination: FilterCombination = FilterCombination.AND
) 