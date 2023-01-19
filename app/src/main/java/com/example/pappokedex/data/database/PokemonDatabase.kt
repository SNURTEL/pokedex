package com.example.pappokedex.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.pappokedex.data.database.entities.*

@Database(
    entities = [
        PokemonEntity::class, PokemonSnapshotEntity::class,
        AbilityEntity::class, PokemonToAbilityEntity::class,
        FavoritePokemon::class
    ],
    version = 3,
    exportSchema = false
)
@TypeConverters(PokemonConverter::class)
abstract class PokemonDatabase : RoomDatabase() {
    abstract fun pokemonDao(): PokemonDao
}