package com.example.pappokedex.ui

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ListTopBar(
    title: String,
    searchBarState: MutableState<TextFieldValue>,
    filterState: MutableState<Map<String, Boolean>>,
    tint: Color = MaterialTheme.colors.primary,
) {
    TopBar(
        title = title,
        isHome = true,
        onNavigateUp = {},
        tint=tint,
        actions = {
            SearchBar(
                state = searchBarState
            )
            FilterIcon(filterState = filterState)
        })
}

@Composable
fun DetailsTopBar(
    title: String,
    tint: Color = MaterialTheme.colors.primary,
    onNavigateUp: () -> Unit
) {
    TopBar(
        title = title,
        isHome = false,
        tint=tint,
        onNavigateUp = onNavigateUp
    ) {}
}

@Composable
fun TopBar(
    title: String,
    isHome: Boolean,
    onNavigateUp: () -> Unit = {},
    tint: Color,
    actions: @Composable() (RowScope.() -> Unit) = {}
) {
    TopAppBar(
        title = { Text(title) },
        backgroundColor = tint,
        contentColor = MaterialTheme.colors.onPrimary,
        navigationIcon = if (!isHome) {
            {
                IconButton(onClick = onNavigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        } else {
            null
        },
        actions = actions
    )
}

@Preview(showBackground = true)
@Composable
fun ListTopBarPreview() {
    val searchState = remember { mutableStateOf(TextFieldValue("")) }
    val filterState = remember {
        mutableStateOf(mapOf("aaa" to true, "bbb" to false))
    }
    ListTopBar(title = "Browse", searchBarState = searchState, filterState = filterState)
}

@Preview(showBackground = true)
@Composable
fun DetailsTopBarPreview() {
    DetailsTopBar(title = "Details", onNavigateUp = {})
}