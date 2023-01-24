package com.example.pappokedex.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_pokemons")
data class FavoritePokemon(
    @PrimaryKey(autoGenerate = false)
    val name: String
)