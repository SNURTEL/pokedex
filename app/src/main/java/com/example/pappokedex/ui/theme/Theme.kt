package com.example.pappokedex.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorPalette = darkColors(
    primary = Purple200,
    primaryVariant = Purple700,
    secondary = Black
)

private val LightColorPalette = lightColors(
    primary = Red,
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
    "normal" to LighterNormal,
    "fire" to LightOrange,
    "water" to LighterBlue,
    "electric" to LightYellow,
    "grass" to LightGreen,
    "ice" to LightBlue2,
    "fighting" to LightRed,
    "poison" to LighterPurple,
    "ground" to LightYellow2,
    "flying" to LightViolet2,
    "psychic" to LighterPink,
    "bug" to LightYellowGreen,
    "rock" to Yellow3,
    "ghost" to LighterGrayViolet,
    "dragon" to LighterViolet,
    "dark" to GrayBrown,
    "steel" to LighterSteel,
    "fairy" to LightPink2
)

val pokemonTypesDict2: MutableMap<String, Color> = hashMapOf(
    "normal" to Normal,
    "fire" to Orange,
    "water" to Blue,
    "electric" to Yellow,
    "grass" to Green,
    "ice" to LightBlue,
    "fighting" to DarkRed,
    "poison" to Purple,
    "ground" to Yellow2,
    "flying" to LightViolet,
    "psychic" to Pink,
    "bug" to YellowGreen,
    "rock" to DarkYellow,
    "ghost" to GrayViolet,
    "dragon" to Violet,
    "dark" to DarkBrown,
    "steel" to Steel,
    "fairy" to LightPink
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