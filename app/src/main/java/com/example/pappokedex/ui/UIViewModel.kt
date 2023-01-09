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
    fun loadSnapshots() =
        viewModelScope.launch { _pokemonSnapshots.value = repository.getPokemonSnapshots() }

    fun getList(): State<List<PokemonSnapshot>> {
        loadSnapshots()
        return pokemonSnapshots
    }

    fun loadPokemon(name: String) =
        viewModelScope.launch { _pokemon.value = repository.getPokemon(name) ?: getNullPokemon() }

    fun getPokemon(name: String): State<Pokemon?> {
        loadPokemon(name)
        return pokemon
    }
}
