package com.example.pappokedex.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.pappokedex.ui.theme.PapPokedexTheme
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
                    FavoritePokemonListInScaffold(navController = findNavController())
                }
            }
        }
    }
}

@Composable
fun FavoritePokemonListInScaffold(
    viewModel: MyViewModel = hiltViewModel(),
    navController: NavController
) {
    Scaffold(
        bottomBar = {
            BottomNav(onSelect = listOf({
                val action =
                    FavoritePokemonsFragmentDirections.actionFavoritePokemonsBottomNavToBrowsePokemonsBottomNav()
                navController.navigate(action)
            }, {}))
        },
        content = {
            FavoritePokemonList(navigateToPokemon = {
                val action =
                    FavoritePokemonsFragmentDirections.actionFavoriteToDisplayPokemonInfo(it)
                navController.navigate(action)
            }, viewModel = viewModel)
            it.calculateTopPadding()
        }
    )
}

@Composable
fun FavoritePokemonList(
    navigateToPokemon: (String) -> Unit,
    viewModel: MyViewModel
) {
    viewModel.loadFavoritesSnapshots()
    PokemonList(viewModel.favoritesSnapshots.value, navigateToPokemon)
}