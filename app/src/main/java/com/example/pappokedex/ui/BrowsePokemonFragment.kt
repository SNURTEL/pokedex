package com.example.pappokedex.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
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
                PapPokedexTheme {
                    BrowsePokemonListInScaffold(navController = findNavController())
                }
            }
        }
    }
}

@Composable
fun BrowsePokemonListInScaffold(
    viewModel: MyViewModel = hiltViewModel(),
    navController: NavController
) {
    Scaffold(
        bottomBar = {
            BottomNav(onSelect = listOf({}, {
                val action =
                    BrowsePokemonFragmentDirections.actionBrowsePokemonsBottomNavToFavoritePokemonsBottomNav()
                navController.navigate(action)
            }))
        },
        content = {
            BrowsePokemonList(navigateToPokemon = {
                val action =
                    BrowsePokemonFragmentDirections.actionBrowsePokemonsFragmentToDisplayPokemonInfo(
                        it
                    )
                navController.navigate(action)
            })
            it.calculateTopPadding()
        }
    )
}

@Composable
fun BrowsePokemonList(
    navigateToPokemon: (String) -> Unit,
    viewModel: MyViewModel = hiltViewModel()
) {
    viewModel.loadAllSnapshots()
    PokemonList(viewModel.pokemonSnapshots.value, navigateToPokemon)
}








