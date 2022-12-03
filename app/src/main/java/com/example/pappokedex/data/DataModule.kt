package com.example.pappokedex.data

import android.content.Context
import androidx.room.Room
import com.example.pappokedex.data.database.PokemonDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    fun providePokemonDao(pokemonDatabase: PokemonDatabase) = pokemonDatabase.pokemonDao()

    @Provides
    @Singleton
    fun providePokemonDatabase(@ApplicationContext applicationContext: Context) =
        Room.databaseBuilder(
            applicationContext,
            PokemonDatabase::class.java, "pokemons-database"
        ).build()
}