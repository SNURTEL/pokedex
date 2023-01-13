package com.example.pappokedex.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import timber.log.Timber


@Preview
@Composable
fun ExpandedSearchViewPreview() {
    val state = remember { mutableStateOf(TextFieldValue("")) }

    Surface(
        color = MaterialTheme.colors.primary
    ) {
        SearchBar(
            state = state,
            expandedInitially = true,
        )
    }
}

@Composable
fun SearchBar(
    expandedInitially: Boolean = false,
    state: MutableState<TextFieldValue>,
) {
    val (expanded, onExpandedChanged) = remember {
        mutableStateOf(expandedInitially)
    }

    Crossfade(targetState = expanded) { isSearchFieldVisible ->
        when (isSearchFieldVisible) {
            true -> ExpandedSearchView(
                state = state,
                onExpandedChanged = onExpandedChanged
            )

            false -> CollapsedSearchView(
                state = state,
                onExpandedChanged = onExpandedChanged
            )
        }
    }
}

@Composable
fun CollapsedSearchView(
    state: MutableState<TextFieldValue>,
    onExpandedChanged: (Boolean) -> Unit,
) {

    Row(
        modifier = Modifier
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = { onExpandedChanged(true) }
        ) {
            Icon(
                Icons.Outlined.Search,
                "search icon",
                tint = MaterialTheme.colors.onPrimary
            )
        }
    }
}

@Composable
fun ExpandedSearchView(
    state: MutableState<TextFieldValue>,
    onExpandedChanged: (Boolean) -> Unit,
) {
    val focusManager = LocalFocusManager.current

    val textFieldFocusRequester = remember { FocusRequester() }
    SideEffect {
        textFieldFocusRequester.requestFocus()
    }

    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = {
            onExpandedChanged(false)
            state.value = TextFieldValue("")
        }) {
            Icon(
                Icons.Outlined.ArrowBack,
                "back icon",
                tint = MaterialTheme.colors.onPrimary
            )
        }
        TextField(
            value = state.value,
            onValueChange = { value ->
                if (state.value.text != value.text ) state.value = value
            },
            trailingIcon = {
                Row {
                    IconButton(onClick = {state.value = TextFieldValue("")}) {
                        Icon(
                            Icons.Outlined.Close,
                            "clear input icon",
                            tint = MaterialTheme.colors.onPrimary
                        )
                    }
                    IconButton(onClick = {
                        focusManager.clearFocus()
                    }) {
                        Icon(
                            Icons.Outlined.Search,
                            "search icon",
                            tint = MaterialTheme.colors.onPrimary
                        )
                    }
                }

            },
            modifier = Modifier
                .focusRequester(textFieldFocusRequester),
            label = {
                Text(text = "Search", color = MaterialTheme.colors.onPrimary)
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }
            )
        )
    }
}
