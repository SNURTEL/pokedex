package com.example.pappokedex.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.fragment.findNavController
import com.example.pappokedex.ui.theme.*
import dagger.hilt.android.AndroidEntryPoint

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
    Scaffold(

        topBar = {
            TopBar()
        }
    ) {
        BrowsePokemonList(navigateToPokemon = navigateToPokemon, viewModel = viewModel)
        it.calculateTopPadding()
    }
}

@Composable
fun BrowsePokemonList(
    navigateToPokemon: (String) -> Unit,
    viewModel: MyViewModel
) {
    remember { viewModel.loadAllSnapshots() }
    val snapshots = viewModel.pokemonSnapshots.collectAsState()
    PokemonList(snapshots.value, navigateToPokemon)
}








