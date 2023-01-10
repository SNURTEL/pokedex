@file:OptIn(ExperimentalMaterialApi::class)

package com.example.pappokedex.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import coil.compose.AsyncImage
import com.example.pappokedex.domain.Pokemon
import com.example.pappokedex.ui.theme.PapPokedexTheme
import com.example.pappokedex.ui.theme.Shapes
import com.example.pappokedex.ui.theme.White
import com.example.pappokedex.ui.theme.getColorFrame
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.*

@AndroidEntryPoint
class DisplayPokemonInfo : Fragment() {
    private val args: DisplayPokemonInfoArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                PapPokedexTheme {
                    PokemonInfoInScaffold(pokemonName = args.pokemonName,
                        navController = findNavController())
                }
            }
        }
    }
}

@Composable
fun PokemonInfoInScaffold(
    pokemonName: String,
    navController: NavController,
    viewModel: MyViewModel = hiltViewModel(),
) {
    val context = LocalContext.current

    viewModel.loadPokemon(pokemonName)
    viewModel.pokemon.value?.let { pokemon ->
        Scaffold(
            floatingActionButton = {
                FavoriteButton(
                    pokemon = pokemon,
                    isFavorite = viewModel.isPokemonInFavorites(pokemon),
                    callback = {
                        val newFavState = !it
                        viewModel.setFavoritePokemon(pokemon, newFavState)
                        Toast.makeText(
                            context,
                            if (newFavState) "%s added to favorites!".format(
                                pokemon.name.uppercase(
                                    Locale.getDefault()
                                )
                            )
                            else "%s removed from favorites!".format(pokemon.name.uppercase(Locale.getDefault())),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )
            },
            bottomBar = {
                BottomNav(onSelect = listOf(
                    {
                        val action =
                            DisplayPokemonInfoDirections.actionDisplayPokemonInfoToBrowsePokemonsBottomNav()
                        navController.navigate(action)
                    },
                    {
                        val action =
                            DisplayPokemonInfoDirections.actionDisplayPokemonInfoToFavoritePokemonsBottomNav()
                        navController.navigate(action)
                    }
                ))
            },
            content = {
                PokemonInfo(pokemonName = pokemonName, viewModel = viewModel)
                it.calculateTopPadding()
            }
        )
    }
}


@Composable
fun PokemonInfo(
    pokemonName: String,
    viewModel: MyViewModel,
) {
    viewModel.loadPokemon(pokemonName)
    viewModel.pokemon.value?.let { pokemon ->
        Timber.tag("POKEMON INFO").d("Reload ${pokemon.name} info page")
        DisplayInfo(pokemon)
    }
}

@Composable
fun FavoriteButton(
    pokemon: Pokemon,
    isFavorite: Boolean,
    callback: (Boolean) -> Unit
) {
    FloatingActionButton(
        onClick = {
            callback(isFavorite)
        },
        backgroundColor = pokemon.types.getOrNull(0)?.let { getColorFrame(it) }
            ?: MaterialTheme.colors.primary,
        contentColor = MaterialTheme.colors.onPrimary,
    ) {
        Icon(
            if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
            ""
        )
    }
}

@Composable
fun DisplayInfo(pokemonInfo: Pokemon) {

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    )
    {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 15.dp)
        ) {
            Text(
                text = pokemonInfo.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
                fontSize = 40.sp,
                style = MaterialTheme.typography.subtitle2,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = 5.dp)
            )
            AsyncImage(
                model = pokemonInfo.iconUrl,
                contentDescription = null,
                modifier = Modifier
                    .padding(top = 150.dp, bottom = 150.dp)
                    .scale(10.0F)
                    .align(Alignment.CenterHorizontally)
            )
            Row {
                Text(
                    text = "Types: ",
                    fontSize = 20.sp,
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier.padding(vertical = 5.dp),
                )
                for (type in pokemonInfo.types) {
                    Surface(
                        shape = MaterialTheme.shapes.medium,
                        elevation = 2.dp,
                        color = getColorFrame(type),
                        modifier = Modifier
                            .padding(horizontal = 5.dp)
                            .border(4.dp, color = getColorFrame(type), Shapes.medium)
                    ) {
                        Text(
                            text = type.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
                            fontSize = 20.sp,
                            color = White,
                            style = MaterialTheme.typography.body2,
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 5.dp),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            Text(
                text = "Height: %.2fm".format(pokemonInfo.height * 0.1),
                fontSize = 20.sp,
                style = MaterialTheme.typography.body2,
                modifier = Modifier.padding(vertical = 6.dp),
            )
            Text(
                text = "Weight: %.2fkg".format(pokemonInfo.weight * 0.1),
                fontSize = 20.sp,
                style = MaterialTheme.typography.body2,
                modifier = Modifier.padding(vertical = 6.dp),
            )
            Text(
                text = "Abilities:",
                fontSize = 20.sp,
                style = MaterialTheme.typography.body2,
                modifier = Modifier.padding(vertical = 5.dp)
            )
            for (ability in pokemonInfo.abilities) {
                ExpandableCard(ability.name, ability.effect_description)
            }
        }
    }
}


@ExperimentalMaterialApi
@Composable
fun ExpandableCard(
    title: String,
    description: String,
) {
    var expandedState by remember { mutableStateOf(false) }
    val rotationState by animateFloatAsState(
        targetValue = if (expandedState) 180f else 0f
    )

    Card(
        modifier = Modifier
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = LinearOutSlowInEasing
                )
            ),
        shape = Shapes.medium,
        onClick = {
            expandedState = !expandedState
        }
    ) {
        Column(
            modifier = Modifier
                .padding(all = 0.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier
                        .padding(all = 2.dp)
                        .weight(9f),
                    text = title.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
                    fontSize = 20.sp,
                    style = MaterialTheme.typography.body2,
                    maxLines = 1,
                )
                IconButton(
                    modifier = Modifier
                        .weight(1f)
                        .alpha(ContentAlpha.medium)
                        .rotate(rotationState),
                    onClick = {
                        expandedState = !expandedState
                    }) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Drop-Down Arrow"
                    )
                }
            }
            if (expandedState) {
                Text(
                    text = description,
                    fontSize = 20.sp,
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier.padding(all = 2.dp),
                )
            }
        }
    }
}