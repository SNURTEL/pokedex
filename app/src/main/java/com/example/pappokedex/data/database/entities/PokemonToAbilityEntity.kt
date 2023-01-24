package com.example.pappokedex.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemon_to_ability")
data class PokemonToAbilityEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val pokemonName: String,
    val abilityName: String
)