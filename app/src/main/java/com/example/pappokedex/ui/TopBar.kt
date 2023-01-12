package com.example.pappokedex.ui

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.pappokedex.R

@Composable
fun TopBar() {
    TopAppBar(
        title = { Text(text = "Hemlo :)") },
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = MaterialTheme.colors.onPrimary,
        actions = {
            ExpandableSearchView(
                initialContent = "",
                onSearchDisplayChanged = {},
                onSearchDisplayClosed = { })
            IconButton(onClick = {/* Do Something*/ }) {
                Icon(painterResource(id = R.drawable.ic_outline_filter_alt_24), null)
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun TopBarPreview() {
    TopBar()
}