package com.example.pappokedex.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.pappokedex.ui.theme.PapPokedexTheme
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.border
import android.content.res.Configuration
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.clickable
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import coil.compose.rememberAsyncImagePainter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope
import coil.compose.AsyncImage
import com.example.pappokedex.domain.Pokemon
import com.example.pappokedex.domain.PokemonSnapshot
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ScrollList : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                PapPokedexTheme() {
                    PokemonList()
                }
            }
        }
    }
}

@Composable
fun PokemonCard(pokemon: PokemonSnapshot) {

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
                    text = pokemon.name,
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
    viewModel: MyViewModel = viewModel()
) {
    LazyColumn {
        val pokemonList = viewModel.getList().value
        pokemonList.map { item { PokemonCard(it) } }
    }
}





