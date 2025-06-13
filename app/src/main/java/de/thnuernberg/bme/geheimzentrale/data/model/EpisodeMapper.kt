package de.thnuernberg.bme.geheimzentrale.data.model

import javax.inject.Inject

/** Maps [EpisodeDto] objects to domain [Episode] instances. */
class EpisodeMapper @Inject constructor() {
    fun fromDto(dto: EpisodeDto): Episode {
        val durationMs = dto.medien
            .flatMap { it.tracks }
            .sumOf { it.end - it.start }
        val coverUrl = when {
            dto.links.cover.isNotEmpty() -> dto.links.cover
            dto.links.cover_kosmos.isNotEmpty() -> dto.links.cover_kosmos
            else -> dto.links.cover_itunes
        }
        val sprecher = dto.sprechrollen.map { it.sprecher }
        return Episode(
            nummer = dto.nummer,
            titel = dto.titel,
            autor = dto.autor,
            hörspielskriptautor = dto.hörspielskriptautor,
            gesamtbeschreibung = dto.gesamtbeschreibung,
            beschreibung = dto.beschreibung,
            veröffentlichungsdatum = dto.veroeffentlichungsdatum.toString(),
            kapitel = dto.kapitel,
            sprechrollen = dto.sprechrollen,
            links = dto.links,
            ids = dto.ids,
            medien = dto.medien,
            erscheinungsdatum = dto.veroeffentlichungsdatum,
            dauer = (durationMs / 1000),
            coverUrl = coverUrl,
            sprecher = sprecher
        )
    }

    fun fromDtoList(list: List<EpisodeDto>): List<Episode> = list.map { fromDto(it) }
}
