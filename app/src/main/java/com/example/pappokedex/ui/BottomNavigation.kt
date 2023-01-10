package com.example.pappokedex.ui

import android.widget.Toast
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview

val items = listOf("Browse", "Favorites")

@Preview
@Composable
fun Prev() {
    val actions = listOf({}, {})
    BottomNav(actions)
}

@Composable
fun BottomNav(onSelect: List<() -> Unit>) {
    val context = LocalContext.current
    BottomNavigation {
        items.forEachIndexed { index, item ->
            BottomNavigationItem(
                icon = { Icon(Icons.Filled.Favorite, contentDescription = null) },
                label = { Text(item) },
                selected = false,
                onClick = onSelect[index] ?: {}

            )
        }
    }
}