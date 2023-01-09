package com.example.pappokedex.ui

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pappokedex.domain.Pokemon
import com.example.pappokedex.domain.PokemonRepository
import com.example.pappokedex.domain.PokemonSnapshot
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: PokemonRepository,
) : ViewModel() {
    private val _pokemonSnapshots = mutableStateOf(emptyList<PokemonSnapshot>())
    val pokemonSnapshots: State<List<PokemonSnapshot>> = _pokemonSnapshots
    private val _pokemon = mutableStateOf<Pokemon?>(null)
    val pokemon: State<Pokemon?> = _pokemon
    private val _favoritesSnapshots = mutableStateOf(emptyList<PokemonSnapshot>())
    val favoritesSnapshots: State<List<PokemonSnapshot>> = _favoritesSnapshots

    fun loadAllSnapshots() =
        viewModelScope.launch { _pokemonSnapshots.value = repository.getAllSnapshots() }

    // todo rewrite explicit getters to be called when accessing a field
    fun getPokemonList(): State<List<PokemonSnapshot>> {
        loadAllSnapshots()
        return pokemonSnapshots
    }

    fun loadPokemon(name: String) =
        viewModelScope.launch { _pokemon.value = repository.getPokemon(name) ?: getNullPokemon() }

    fun getPokemon(name: String): State<Pokemon?> {
        loadPokemon(name)
        return pokemon
    }

    fun loadFavoritesSnapshots() =
        viewModelScope.launch { _favoritesSnapshots.value = repository.getFavoriteSnapshots() }

    fun isPokemonInFavorites(pokemon: Pokemon): Boolean {
        loadFavoritesSnapshots()
        return favoritesSnapshots.value
            .map { it.name }
            .contains(pokemon.name)
    }

    fun setFavoritePokemon(pokemon: Pokemon, isFavorite: Boolean) =
        viewModelScope.launch {
            if (isFavorite) repository.setFavoritePokemon(pokemon.name) else repository.removeFavoritePokemon(
                pokemon.name
            )
            loadFavoritesSnapshots()
        }

}
