package com.example.pappokedex.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.input.TextFieldValue
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.fragment.findNavController
import com.example.pappokedex.domain.PokemonSnapshot
import com.example.pappokedex.ui.theme.PapPokedexTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class FavoritePokemonsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                PapPokedexTheme() {
                    FavoritePokemonListScaffold(
                        "Favorites",
                        ::navigateToDetails)
                }
            }
        }
    }

    private fun navigateToDetails(pokemonName: String) {
        val action =
            FavoritePokemonsFragmentDirections
                .actionFavoriteToDisplayPokemonInfo(pokemonName)
        findNavController().navigate(action)
    }
}

@Composable
fun FavoritePokemonListScaffold(
    title: String,
    navigateToPokemon: (String) -> Unit,
    viewModel: MyViewModel = hiltViewModel()
) {
    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(
        color = MaterialTheme.colors.primary
    )

    val searchBarState = remember { mutableStateOf(TextFieldValue("")) }
    remember { viewModel.loadAllSnapshots() }
    val filterState = remember {
        mutableStateOf(mapOf<String, Boolean>())
    }

    val snapshotsFlow = viewModel.favouritesSnapshots
    if(filterState.value.isEmpty()) {
        val types = snapshotsFlow.collectAsState().value
            .flatMap { it.types }
            .distinct()
        filterState.value = types.map { it to true }.toMap()

    }

    Scaffold(
        topBar = {
            ListTopBar(
                title = title,
                searchBarState = searchBarState,
                filterState = filterState
            )
        }
    ) {
        val text = searchBarState.value.text
        Timber.tag("SELECTOR").d(text)
        FavoritePokemonList(
            navigateToPokemon = navigateToPokemon,
            filterSelector = { p ->
                p.name.startsWith(text) and p.types.any { filterState.value.getOrElse(it) {false} }
            },
            viewModel = viewModel
        )
        it.calculateTopPadding()
    }
}

@Composable
fun FavoritePokemonList(
    navigateToPokemon: (String) -> Unit,
    sortSelector: ((PokemonSnapshot) -> Comparable<Any>?)? = null,
    filterSelector: ((PokemonSnapshot) -> Boolean) = { true },
    viewModel: MyViewModel = hiltViewModel()
) {
    remember { viewModel.loadFavoritesSnapshots() }
    val snapshots = viewModel.favouritesSnapshots.collectAsState()
    PokemonList(
        snapshots = snapshots.value.filter(filterSelector),
        navigateToPokemon = navigateToPokemon
    )}