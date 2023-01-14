package com.example.pappokedex.ui

import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pappokedex.domain.Pokemon
import com.example.pappokedex.domain.PokemonRepository
import com.example.pappokedex.domain.PokemonSnapshot
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMap
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: PokemonRepository,
) : ViewModel() {
    val pokemonSnapshots: StateFlow<List<PokemonSnapshot>> = repository.snapshotsFlow
    val favouritesSnapshots: StateFlow<List<PokemonSnapshot>> = repository.favouritesFlow

    private val _pokemon = mutableStateOf<Pokemon?>(null)
    val pokemon: State<Pokemon?> = _pokemon


    val types = mutableStateOf<List<String>?>(null)

    fun loadAllSnapshots() =
        viewModelScope.launch { repository.getAllSnapshots() }

    fun loadPokemon(name: String) =
        viewModelScope.launch { _pokemon.value = repository.getPokemon(name) ?: getNullPokemon() }

    fun getPokemon(name: String): State<Pokemon?> {
        loadPokemon(name)
        return pokemon
    }

    fun loadFavoritesSnapshots() =
        viewModelScope.launch { repository.getFavoriteSnapshots() }

    fun isPokemonInFavorites(pokemon: Pokemon): Boolean {
        loadFavoritesSnapshots()
        return favouritesSnapshots.value
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
