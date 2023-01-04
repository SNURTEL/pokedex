package com.example.pappokedex.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = Purple200,
    primaryVariant = Purple700,
    secondary = Black
)

private val LightColorPalette = lightColors(
    primary = Purple500,
    primaryVariant = Purple700,
    secondary = Black,

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)
fun getColorBackground(type:String): Color {
    return pokemonTypesDict1[type] ?: White
}
fun getColorFrame(type:String): Color {
    return  pokemonTypesDict2[type] ?: White
}
val pokemonTypesDict1: MutableMap<String, Color> = hashMapOf(
    "grass" to Green,
    "fire" to Red2,
    "water" to Blue,
    "bug" to LightGreen,
    "normal" to Normal,
    "electric" to Yellow,
    "poison" to Purple,
    "flying" to LightBlue,
    "ground" to Brown
)

val pokemonTypesDict2: MutableMap<String, Color> = hashMapOf(
    "grass" to Green2,
    "fire" to Red,
    "water" to Blue2,
    "bug" to Green3,
    "normal" to Normal2,
    "electric" to Yellow2,
    "poison" to Purple2,
    "flying" to LightBlue,
    "ground" to Brown2
)

@Composable
fun PapPokedexTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}