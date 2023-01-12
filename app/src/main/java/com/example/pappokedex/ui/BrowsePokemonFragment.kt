package com.example.pappokedex.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.*
import androidx.compose.ui.platform.ComposeView
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
                    BrowsePokemonList(::navigateToDetails)
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
fun BrowsePokemonList(
    navigateToPokemon: (String) -> Unit,
    viewModel: MyViewModel = hiltViewModel()
) {
    remember { viewModel.loadAllSnapshots() }
    val snapshots = viewModel.pokemonSnapshots.collectAsState()
    PokemonList(snapshots.value, navigateToPokemon)
}








