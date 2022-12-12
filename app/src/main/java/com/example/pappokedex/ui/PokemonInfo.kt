package com.example.pappokedex.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.fragment.navArgs
import coil.compose.AsyncImage
import com.example.pappokedex.domain.Ability
import com.example.pappokedex.domain.Pokemon
import com.example.pappokedex.ui.theme.PapPokedexTheme
import dagger.hilt.android.AndroidEntryPoint

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

fun dataInfo(): Pokemon {
    val run = Ability("Run Away", "Ensures success fleeing from wild battles.")
    val adapt = Ability("Adaptability", "Increases the same-type attack bonus from 1.5× to 2×.")
    val abilities: List<Ability> = listOf(run, adapt)
    val pokType: List<String> = listOf("Normal")
    return Pokemon(
        "Eevee",
        "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/133.png",
        3,
        65,
        abilities,
        pokType,
        69,
        true,
        false,
        false
    )
}

@Composable
fun DisplayInfo(pokemonInfo: Pokemon) {

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    )
    {
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            AsyncImage(
                model = pokemonInfo.iconUrl,
                contentDescription = null,
                modifier = Modifier
                    .padding(top = 100.dp, bottom = 60.dp)
                    .scale(10.0F)
                    .align(Alignment.CenterHorizontally)
            )
            Text(
                text = "Name: ${pokemonInfo.name}",
                fontSize = 20.sp
            )
            Text(
                text = "Height: %.2fm".format(pokemonInfo.height * 0.1),
                fontSize = 20.sp
            )
            Text(
                text = "Weight: %.2fkg".format(pokemonInfo.weight * 0.1),
                fontSize = 20.sp
            )
            Text(
                text = pokemonInfo.types.joinToString(prefix = "Types: ", separator = ", "),
                fontSize = 20.sp
            )
            Text(
                text = "Abilities:",
                fontSize = 20.sp
            )
            for (ability in pokemonInfo.abilities) {
                Text(
                    text = "• ${ability.name}",
                    fontSize = 20.sp
                )
                Text(
                    ability.effect_description,
                    fontSize = 20.sp
                )
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

    DisplayedPokemonInfo(viewModel.pokemon.value)
}

@Composable
fun DisplayedPokemonInfo(pokemon: Pokemon?) {

}