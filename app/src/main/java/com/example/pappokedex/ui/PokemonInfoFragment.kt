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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.example.pappokedex.domain.Pokemon
import com.example.pappokedex.ui.theme.*
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.*

@AndroidEntryPoint
class DisplayPokemonInfo : Fragment() {
    val args: DisplayPokemonInfoArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                PapPokedexTheme {
                    PokemonInfoScaffold(
                        pokemonName = args.pokemonName,
                        onNavigateUp = {
                            findNavController().navigateUp()
                        }
                    )
                }
            }
        }
    }
}


@Composable
fun PokemonInfoScaffold(
    pokemonName: String,
    onNavigateUp: () -> Unit,
    viewModel: MyViewModel = hiltViewModel(),
) {
    val context = LocalContext.current

    viewModel.loadPokemon(pokemonName)
    viewModel.pokemon.value?.let { pokemon ->
        remember { viewModel.loadFavoritesSnapshots() }

        val tint =
            pokemon.types.getOrNull(0)?.let { getColorFrame(it) } ?: MaterialTheme.colors.onPrimary

        val systemUiController = rememberSystemUiController()
        systemUiController.setStatusBarColor(
            color = tint
        )

        Scaffold(
            floatingActionButton = {
                FavoriteButton(
                    isFavorite = viewModel.favouritesSnapshots.collectAsState().value.any { it.name == pokemonName },
                    setFavorite = {
                        val newFav = !viewModel.isPokemonInFavorites(pokemon)
                        viewModel.setFavoritePokemon(pokemon, newFav)
                        Toast.makeText(
                            context,
                            if (newFav) "%s added to favorites!".format(
                                pokemon.name.uppercase(
                                    Locale.getDefault()
                                )
                            )
                            else "%s removed from favorites!".format(pokemon.name.uppercase(Locale.getDefault())),
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    tint = tint
                )
            },
            topBar = {
                DetailsTopBar(
                    title = "Details",
                    onNavigateUp = onNavigateUp,
                    tint = tint
                )
            },
            content = {
                DisplayInfo(pokemon)
                it.calculateTopPadding()
            }
        )
    }
}

@Composable
fun FavoriteButton(
    isFavorite: Boolean,
    setFavorite: (Boolean) -> Unit,
    tint: Color?
) {
    FloatingActionButton(
        onClick = {
            val newFavState = !isFavorite
            setFavorite(newFavState)
        },
        backgroundColor = tint ?: MaterialTheme.colors.primary,
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
//        color = MaterialTheme.colors.background
        color = getColorBackground(pokemonInfo.types[0])
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
            SubcomposeAsyncImage(
                model = pokemonInfo.iconUrl,
                contentDescription = null,
                filterQuality = FilterQuality.None,
                modifier = Modifier
                    .padding(top = 150.dp, bottom = 150.dp)
                    .scale(10.0F)
                    .align(Alignment.CenterHorizontally)
            ) {
                val state = painter.state
                if (state is AsyncImagePainter.State.Loading || state is AsyncImagePainter.State.Error) {
                    CircularProgressIndicator()
                } else {
                    SubcomposeAsyncImageContent()
                }
            }
            Row() {
                Text(
                    text = "Types: ",
                    fontSize = 25.sp,
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
                fontSize = 25.sp,
                style = MaterialTheme.typography.body2,
                modifier = Modifier.padding(vertical = 6.dp),
            )
            Text(
                text = "Weight: %.2fkg".format(pokemonInfo.weight * 0.1),
                fontSize = 25.sp,
                style = MaterialTheme.typography.body2,
                modifier = Modifier.padding(vertical = 6.dp),
            )
            Text(
                text = "Abilities:",
                fontSize = 25.sp,
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
        backgroundColor = Color.Transparent,
        elevation = 0.dp,
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
                    fontSize = 25.sp,
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
                    fontSize = 22.sp,
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier.padding(all = 2.dp),
                )
            }
        }
    }
}