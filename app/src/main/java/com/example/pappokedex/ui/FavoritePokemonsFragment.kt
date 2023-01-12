package com.example.pappokedex.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.fragment.findNavController
import com.example.pappokedex.ui.theme.PapPokedexTheme
import dagger.hilt.android.AndroidEntryPoint

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
                    FavoritePokemonList(::navigateToDetails)
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
fun FavoritePokemonList(
    navigateToPokemon: (String) -> Unit,
    viewModel: MyViewModel = hiltViewModel()
) {
    remember { viewModel.loadFavoritesSnapshots() }
    val snapshots = viewModel.favouritesSnapshots.collectAsState()
    PokemonList(snapshots.value, navigateToPokemon)
}