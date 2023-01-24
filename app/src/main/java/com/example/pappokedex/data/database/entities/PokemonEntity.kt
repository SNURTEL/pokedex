package com.example.pappokedex.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemons")
data class PokemonEntity(
    @PrimaryKey(autoGenerate = false)
    val name: String,
    val iconUrl: String,
    val height: Int,
    val weight: Int,
    val types: List<String>,
    val evolutionChainId: Int?,
    val isBaby: Boolean,
    val isLegendary: Boolean,
    val isMythical: Boolean
) {
    fun toSnapshot() = PokemonSnapshotEntity(name, iconUrl, types)
}