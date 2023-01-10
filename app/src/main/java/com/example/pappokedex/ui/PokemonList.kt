package com.example.pappokedex.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.pappokedex.domain.PokemonSnapshot
import com.example.pappokedex.ui.theme.Shapes
import com.example.pappokedex.ui.theme.White
import com.example.pappokedex.ui.theme.getColorFrame
import java.util.*


@Composable
fun PokemonList(snapshots: List<PokemonSnapshot>, navigateToPokemon: (String) -> Unit) {
    LazyColumn() {
        items(snapshots) {
            PokemonListEntry(it, navigateToPokemon)
        }
    }
}


@Composable
fun PokemonListEntry(pokemon: PokemonSnapshot, navigateToPokemon: (String) -> Unit, isFavorite: Boolean = false) {

    // Add padding
    var isExpanded by remember { mutableStateOf(false) }
    // surfaceColor will be updated gradually from one color to the other
    val surfaceColor by animateColorAsState(
        if (isExpanded) MaterialTheme.colors.primary else MaterialTheme.colors.surface,
    )

    Surface(
        shape = MaterialTheme.shapes.medium,
//        elevation = 1.dp,
        // surfaceColor color will be changing gradually from primary to surface
        //color = surfaceColor,
        color = Color.Transparent,
        // animateContentSize will change the Surface size gradually
        modifier = Modifier
            .animateContentSize()
//            .padding(1.dp)
            .clickable { navigateToPokemon(pokemon.name) }
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        White,
                        getColorFrame(pokemon.types[0])
                    )
                )
            )
    ) {

        Spacer(modifier = Modifier.fillMaxWidth())

        Row(modifier = Modifier
            .padding(all = 8.dp)
            .clickable { navigateToPokemon(pokemon.name) }
        ) {

            AsyncImage(
                model = pokemon.iconUrl,
                contentDescription = "Pokemon Icon",
                modifier = Modifier
                    // Set image size to 40 dp
                    .size(70.dp)
                    // Clip image to be shaped as a circle
                    .clip(CircleShape)
                    .border(3.dp, getColorFrame(pokemon.types[0]), CircleShape)
                    .clickable { navigateToPokemon(pokemon.name) }
            )

            // Add a horizontal space between the image and the column
            Spacer(modifier = Modifier.width(5.dp))


            // We toggle the isExpanded variable when we click on this Column
            Column(modifier = Modifier.clickable { isExpanded = !isExpanded }) {
                Text(
                    text = pokemon.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
                    fontSize = 20.sp,
                    color = MaterialTheme.colors.secondary,
                    style = MaterialTheme.typography.subtitle2,
                    modifier = Modifier
                        .padding(horizontal = 6.dp)
                        .clickable { navigateToPokemon(pokemon.name) }

                )
                // Add a vertical space between the Name and types
                Spacer(modifier = Modifier.height(4.dp))

                Row(modifier = Modifier.padding(horizontal = 1.dp)) {
                    for (type in pokemon.types) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            shape = MaterialTheme.shapes.medium,
                            elevation = 2.dp,
                            color = getColorFrame(type),
                            modifier = Modifier
                                .padding(horizontal = 1.dp)
                                .border(3.dp, color = getColorFrame(type), Shapes.medium)
                        ) {
                            Text(
                                text = type.replaceFirstChar {
                                    if (it.isLowerCase()) it.titlecase(
                                        Locale.getDefault()
                                    ) else it.toString()
                                },
                                fontSize = 15.sp,
                                color = White,
                                modifier = Modifier.padding(all = 5.dp),
                                // If the message is expanded, we display all its content
                                // otherwise we only display the first line
                                maxLines = if (isExpanded) Int.MAX_VALUE else 1,
                                style = MaterialTheme.typography.body2,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

            }
            // todo display favorite icon when needed
//            Spacer(Modifier.weight(1f))
//            Icon(if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder, "", Modifier
//                .align(Alignment.CenterVertically)
//                .padding(16.dp)
//                .size(30.dp),
//            tint = if (isFavorite) Color.White else Color.Transparent)
        }
    }
}

