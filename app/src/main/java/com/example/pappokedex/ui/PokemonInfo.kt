@file:OptIn(ExperimentalMaterialApi::class)

package com.example.pappokedex.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.fragment.navArgs
import coil.compose.AsyncImage
import com.example.pappokedex.domain.Pokemon
import com.example.pappokedex.ui.theme.PapPokedexTheme
import com.example.pappokedex.ui.theme.Shapes
import com.example.pappokedex.ui.theme.getColorFrame
import dagger.hilt.android.AndroidEntryPoint
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
                    PokemonInfo(args.pokemonName)
                }
            }
        }
    }
}

@Composable
fun DisplayInfo(pokemonInfo: Pokemon) {

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    )
    {
        Column(modifier = Modifier.verticalScroll(rememberScrollState()).padding(horizontal = 15.dp)) {
            Text(
                text = pokemonInfo.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
                fontSize = 40.sp,
                style = MaterialTheme.typography.subtitle2,
                modifier = Modifier.align(Alignment.CenterHorizontally).padding(vertical = 5.dp)
            )
            AsyncImage(
                model = pokemonInfo.iconUrl,
                contentDescription = null,
                modifier = Modifier
                    .padding(top = 150.dp, bottom = 150.dp)
                    .scale(10.0F)
                    .align(Alignment.CenterHorizontally)
            )
            Row() {
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
                        modifier = Modifier.padding(horizontal = 5.dp).border(4.dp, color = getColorFrame(type), Shapes.medium)
                    ) {
                        Text(
                            text = type.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
                            fontSize = 20.sp,
                            style = MaterialTheme.typography.body2,
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 5.dp),
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

@Composable
fun PokemonInfo(
    pokemonName: String,
    viewModel: MyViewModel = hiltViewModel(),
) {
    viewModel.loadPokemon(pokemonName)
    viewModel.pokemon.value?.let { pokemon ->
        DisplayInfo(pokemon)
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