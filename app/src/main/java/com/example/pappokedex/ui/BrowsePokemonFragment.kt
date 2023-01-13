package com.example.pappokedex.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.fragment.findNavController
import com.example.pappokedex.domain.PokemonSnapshot
import com.example.pappokedex.ui.theme.*
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
                    BrowsePokemonListScaffold(::navigateToDetails)
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
    navigateToPokemon: (String) -> Unit,
    viewModel: MyViewModel = hiltViewModel()
) {
    val searchBarState = remember { mutableStateOf(TextFieldValue("")) }
    Timber.tag("SCAFFOLD").d("FULL RECOMPOSITION")
    Scaffold(
        topBar = {
            TopBar(
                searchBarState = searchBarState
            )
        }
    ) {
        val text = searchBarState.value.text
        Timber.tag("SELECTOR").d(text)
        BrowsePokemonList(
            navigateToPokemon = navigateToPokemon,
            filterSelector = { p ->
                p.name.startsWith(text)
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
    Timber.tag("BROWSE").d("RECOMPOSITION")
    remember { viewModel.loadAllSnapshots() }
    val snapshots = viewModel.pokemonSnapshots.collectAsState()
    PokemonList(
        snapshots = snapshots.value.filter(filterSelector),
        navigateToPokemon = navigateToPokemon
    )
}








