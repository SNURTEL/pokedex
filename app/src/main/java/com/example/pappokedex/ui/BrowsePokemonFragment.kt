package com.example.pappokedex.ui

import android.content.res.Resources.Theme
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.input.TextFieldValue
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.fragment.findNavController
import com.example.pappokedex.domain.PokemonSnapshot
import com.example.pappokedex.ui.theme.*
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class BrowsePokemonFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                PapPokedexTheme() {
                    BrowsePokemonListScaffold(
                        "Browse",
                        ::navigateToDetails
                    )
                }
            }
        }
    }

    private fun navigateToDetails(pokemonName: String) {
        val action =
            BrowsePokemonFragmentDirections.actionBrowsePokemonsFragmentToDisplayPokemonInfo(
                pokemonName
            )
        findNavController().navigate(action)
    }
}

@Composable
fun BrowsePokemonListScaffold(
    title: String,
    navigateToPokemon: (String) -> Unit,
    viewModel: MyViewModel = hiltViewModel()
) {
    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(
        color = MaterialTheme.colors.primary
    )

    val searchBarState = remember { mutableStateOf(TextFieldValue("")) }
    val filterState = remember {
        mutableStateOf(mapOf<String, Boolean>())
    }

    val snapshotsFlow = viewModel.pokemonSnapshots
    if (filterState.value.isEmpty()) {
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
        BrowsePokemonList(
            navigateToPokemon = navigateToPokemon,
            filterSelector = { p ->
                p.name.startsWith(text) and p.types.any { filterState.value.getOrElse(it) { false } }
            },
            viewModel = viewModel
        )
        it.calculateTopPadding()
    }
}

@Composable
fun BrowsePokemonList(
    navigateToPokemon: (String) -> Unit,
    sortSelector: ((PokemonSnapshot) -> Comparable<Any>?)? = null,
    filterSelector: ((PokemonSnapshot) -> Boolean) = { true },
    viewModel: MyViewModel
) {
    remember { viewModel.loadAllSnapshots() }
    val snapshots = viewModel.pokemonSnapshots.collectAsState()
    PokemonList(
        snapshots = snapshots.value.filter(filterSelector),
        navigateToPokemon = navigateToPokemon
    )
}








