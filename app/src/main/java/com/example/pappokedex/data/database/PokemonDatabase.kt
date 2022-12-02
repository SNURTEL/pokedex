package com.example.pappokedex.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.pappokedex.data.database.entities.AbilityEntity
import com.example.pappokedex.data.database.entities.PokemonEntity
import com.example.pappokedex.data.database.entities.PokemonSnapshotEntity
import com.example.pappokedex.data.database.entities.PokemonToAbilityEntity

@Database(
    entities = [
        PokemonEntity::class, PokemonSnapshotEntity::class,
        AbilityEntity::class, PokemonToAbilityEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(PokemonConverter::class)
abstract class PokemonDatabase : RoomDatabase() {
    abstract fun pokemonDao(): PokemonDao
}