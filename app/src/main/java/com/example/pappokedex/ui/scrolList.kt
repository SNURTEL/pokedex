package com.example.pappokedex.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.fragment.findNavController
import coil.compose.AsyncImage
import com.example.pappokedex.domain.PokemonSnapshot
import com.example.pappokedex.ui.theme.PapPokedexTheme
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class ScrollList : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                PapPokedexTheme() {
                    PokemonList(::navigateToDetails)
                }
            }
        }
    }

    private fun navigateToDetails(pokemonName: String) {
        val action =
            ScrollListDirections
                .actionScrollListToDisplayPokemonInfo(pokemonName)
        findNavController().navigate(action)
    }
}

@Composable
fun PokemonCard(pokemon: PokemonSnapshot, navigateToPokemon: (String) -> Unit) {

    // Add padding
    var isExpanded by remember { mutableStateOf(false) }
    // surfaceColor will be updated gradually from one color to the other
    val surfaceColor by animateColorAsState(
        if (isExpanded) MaterialTheme.colors.primary else MaterialTheme.colors.surface,
    )

    Surface(
        shape = MaterialTheme.shapes.medium,
        elevation = 1.dp,
        // surfaceColor color will be changing gradually from primary to surface
        color = surfaceColor,
        // animateContentSize will change the Surface size gradually
        modifier = Modifier
            .animateContentSize()
            .padding(1.dp)
            .clickable { navigateToPokemon(pokemon.name) }
    ) {

        Spacer(modifier = Modifier.fillMaxWidth())

        Row(modifier = Modifier.padding(all = 8.dp)) {

            AsyncImage(
                model = pokemon.iconUrl,
                contentDescription = "Pokemon Icon",
                modifier = Modifier
                    // Set image size to 40 dp
                    .size(40.dp)
                    // Clip image to be shaped as a circle
                    .clip(CircleShape)
                    .border(1.5.dp, MaterialTheme.colors.secondary, CircleShape)
            )

            // Add a horizontal space between the image and the column
            Spacer(modifier = Modifier.width(5.dp))


            // We toggle the isExpanded variable when we click on this Column
            Column(modifier = Modifier.clickable { isExpanded = !isExpanded }) {
                Text(
                    text = pokemon.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
                    color = MaterialTheme.colors.secondaryVariant,
                    style = MaterialTheme.typography.subtitle2

                )
                // Add a vertical space between the Name and types
                Spacer(modifier = Modifier.height(4.dp))

                Row(modifier = Modifier.padding(all = 8.dp)) {
                    for (i in pokemon.types) {
                        Spacer(modifier = Modifier.width(5.dp))
                        Surface(shape = MaterialTheme.shapes.medium, elevation = 1.dp) {
                            Text(
                                text = i,
                                modifier = Modifier.padding(all = 4.dp),
                                // If the message is expanded, we display all its content
                                // otherwise we only display the first line
                                maxLines = if (isExpanded) Int.MAX_VALUE else 1,
                                style = MaterialTheme.typography.body2
                            )
                        }
                    }
                }

            }
        }
    }
}


@Composable
fun PokemonList(
    navigateToPokemon: (String) -> Unit,
    viewModel: MyViewModel = hiltViewModel()
) {
    viewModel.loadSnapshots()
    PokemonSnapshots(viewModel.pokemonSnapshots.value, navigateToPokemon)
}

@Composable
fun PokemonSnapshots(snapshots: List<PokemonSnapshot>, navigateToPokemon: (String) -> Unit) {
    LazyColumn() {
        items(snapshots) {
            PokemonCard(it, navigateToPokemon)
        }
    }
}





