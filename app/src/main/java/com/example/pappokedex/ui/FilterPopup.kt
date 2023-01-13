package com.example.pappokedex.ui

import android.widget.Space
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.example.pappokedex.R

@Composable
fun FilterIcon(filterState: MutableState<Map<String, Boolean>>) {
    val isPopupShowing = remember { mutableStateOf(false) }
    IconButton(onClick = { isPopupShowing.value = !isPopupShowing.value }) {
        if (isPopupShowing.value) {
            // todo animate
            FilterPopup(isPopupShowing, filterState)
//            Box(
//                modifier = Modifier
//                    .background(Color.Black.copy(alpha = 0.1f))
//                    .fillMaxSize()
//            )
        }
        Icon(painterResource(id = R.drawable.ic_outline_filter_alt_24), null)
    }
}

@Composable
fun FilterPopup(isShowing: MutableState<Boolean>, filterState: MutableState<Map<String, Boolean>>) {
    val innerState = remember { mutableStateOf(filterState.value) }
    Popup(
        alignment = Alignment.Center,
        onDismissRequest = { isShowing.value = false },
        properties = PopupProperties(
            focusable = true,
        )
    ) {
        FilterPopupContent(
            innerState = innerState,
            outerState = filterState,
            isShowing = isShowing
        )
    }


}

@Composable
fun FilterPopupContent(
    innerState: MutableState<Map<String, Boolean>>,
    outerState: MutableState<Map<String, Boolean>>,
    isShowing: MutableState<Boolean>
) {
    val shape = RoundedCornerShape(30.dp)
    Card(
        shape = shape,
        modifier = Modifier
            .clip(shape)
            .width(300.dp)
            .background(MaterialTheme.colors.background)
    ) {
        val buttonStyle = TextStyle(
            color = MaterialTheme.colors.primary,
            fontSize = MaterialTheme.typography.button.fontSize
        )
        Column(Modifier, verticalArrangement = Arrangement.Top) {
            Text(
                text = "Show types",
                style = TextStyle(fontSize = 20.sp),
                modifier = Modifier
                    .padding(24.dp, 24.dp, 0.dp, 12.dp)
            )
            Column(Modifier.heightIn(0.dp, 500.dp)) {
                Column(Modifier.verticalScroll(rememberScrollState())) {
                    FilterList(items = innerState)
                }
            }

            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {

                ClickableText(
                    text = AnnotatedString("RESET"),
                    style = buttonStyle,
                    modifier = Modifier.padding(24.dp),
                    onClick = {
                        innerState.value =
                            innerState.value.entries.map { (k, v) -> k to true }.toMap()
                    })
                Spacer(Modifier.weight(1f))
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ClickableText(
                        text = AnnotatedString("CANCEL"),
                        style = buttonStyle,
                        modifier = Modifier.padding(24.dp),
                        onClick = { isShowing.value = false })
                    ClickableText(
                        text = AnnotatedString("APPLY"),
                        style = buttonStyle,
                        modifier = Modifier.padding(24.dp),
                        onClick = {
                            outerState.value = innerState.value
                            isShowing.value = false
                        })
                }
            }
        }
    }
}

@Composable
fun FilterList(items: MutableState<Map<String, Boolean>>) {
    Column(
    ) {
        items.value.entries.sortedBy { it.key }.map { (label, selected) ->
            FilterListEntry(
                label = label,
                isSelected = selected,
                onSelectionChanged = {
                    // must be reassigned explicitly to guarantee recomposition
                    items.value =
                        items.value.entries.associate { (k, v) -> k to if (k == label) !v else v }
                }
            )
        }

    }

}

@Composable
fun FilterListEntry(
    label: String,
    isSelected: Boolean,
    onSelectionChanged: (Boolean) -> Unit
) {
    Surface(Modifier.fillMaxWidth().clickable { onSelectionChanged(isSelected) }) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Checkbox(
                checked = isSelected,
                onCheckedChange = onSelectionChanged,
                modifier = Modifier.padding(start = 12.dp, end = 8.dp)
            )
            Text(
                text = label.replaceFirstChar(Char::titlecase),
                style = TextStyle(fontSize = 16.sp),
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun FilterPopupPreview() {
    val isPopupShowing = remember { mutableStateOf(true) }

    val outerState = remember {
        mutableStateOf(
            mapOf(
                Pair("Hemlo", true),
                Pair("Kotlin", true),
                Pair("world!", false),
            )
        )
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
    ) {
        if (isPopupShowing.value) {
            // todo animate
            FilterPopup(isPopupShowing, outerState)
        }

    }
}

