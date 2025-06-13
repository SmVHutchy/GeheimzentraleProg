package de.thnuernberg.bme.geheimzentrale.data.model

import com.google.gson.annotations.SerializedName
import java.time.LocalDate

/**
 * DTOs mirroring the structure of the episode JSON file.
 */

data class ApiResponseDto(
    val dbInfo: DbInfo,
    val serie: List<EpisodeDto>
)

/** Episode structure as stored in the JSON file */
data class EpisodeDto(
    val nummer: Int,
    val titel: String,
    val autor: String,
    val hörspielskriptautor: String,
    val gesamtbeschreibung: String,
    val beschreibung: String,
    @SerializedName("veröffentlichungsdatum")
    val veroeffentlichungsdatum: LocalDate,
    val kapitel: List<Kapitel>,
    val sprechrollen: List<Sprechrolle>,
    val links: Links,
    val ids: Ids,
    val medien: List<Medium>
)
