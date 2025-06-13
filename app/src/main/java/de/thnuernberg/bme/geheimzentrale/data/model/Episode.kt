package de.thnuernberg.bme.geheimzentrale.data.model

import java.time.LocalDate

data class DbInfo(
    val version: String,
    val lastModified: String
)

data class Episode(
    val nummer: Int,
    val titel: String,
    val autor: String,
    val hörspielskriptautor: String,
    val gesamtbeschreibung: String,
    val beschreibung: String,
    val veröffentlichungsdatum: String,
    val kapitel: List<Kapitel>,
    val sprechrollen: List<Sprechrolle>,
    val links: Links,
    val ids: Ids,
    val medien: List<Medium>,
    val erscheinungsdatum: LocalDate,
    val dauer: Int, // in Sekunden
    val coverUrl: String,
    val sprecher: List<String>
)

data class Kapitel(
    val titel: String,
    val start: Int, // in Millisekunden
    val end: Int // in Millisekunden
)

data class Sprechrolle(
    val rolle: String,
    val sprecher: String,
    val pseudonym: String? = null
)

data class Links(
    val json: String,
    val ffmetadata: String,
    val cover: String,
    val cover_itunes: String,
    val cover_kosmos: String,
    val dreifragezeichen: String,
    val appleMusic: String? = null,
    val spotify: String? = null,
    val bookbeat: String? = null,
    val amazonMusic: String? = null,
    val amazon: String? = null,
    val youTubeMusic: String? = null
)

data class Ids(
    val dreimetadaten: Int,
    val appleMusic: String? = null,
    val spotify: String? = null,
    val bookbeat: String? = null,
    val amazonMusic: String? = null,
    val amazon: String? = null,
    val youTubeMusic: String? = null
)

data class Medium(
    val tracks: List<Track>
)

data class Track(
    val titel: String,
    val start: Int,
    val end: Int
)

data class StreamingLinks(
    val spotify: String? = null,
    val appleMusic: String? = null,
    val amazonMusic: String? = null,
    val youTubeMusic: String? = null
)

data class ApiResponse(
    val dbInfo: DbInfo,
    val serie: List<Episode>
) 