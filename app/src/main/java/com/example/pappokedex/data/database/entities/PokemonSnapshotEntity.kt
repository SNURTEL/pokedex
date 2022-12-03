package com.example.pappokedex.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemon_snapshots")
data class PokemonSnapshotEntity(
    @PrimaryKey(autoGenerate = false)
    val name: String,
    val iconUrl: String,
    val types: List<String>,
)