package com.umno.digital.composepractice.task1

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.umno.digital.composepractice.MAIN_SCREEN
import com.umno.digital.composepractice.R
import com.umno.digital.composepractice.data.TextItemData
import com.umno.digital.composepractice.data.createTextInputList
import com.umno.digital.composepractice.ui.theme.ComposePracticeTheme

@Preview(
    showBackground = true,
    showSystemUi = true,
    uiMode = UI_MODE_NIGHT_YES,
    name = "nightMode"
)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ListOfTextInputScreenPreview() {
    ComposePracticeTheme {
        ListOfTextInputsScreen(
            PaddingValues(),
            rememberNavController(),
        )
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ListOfTextInputsScreen(
    paddingValues: PaddingValues,
    navController: NavHostController,
) {
    var textInputList by rememberSaveable { mutableStateOf(createTextInputList()) }
    val showFABs = remember { mutableStateOf(false) }
    val openDialog = rememberSaveable { mutableStateOf(false) }
    val resultText = rememberSaveable { mutableStateOf("") }
    val deletedItems = remember { mutableStateListOf<TextItemData>() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current // получение контекста
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.task1_screen_title),
                        color = Color.White,
                        fontSize = 20.sp
                    )
                },
                backgroundColor = MaterialTheme.colorScheme.primary,
                navigationIcon = {
                    IconButton(
                        onClick = { navController.navigate(MAIN_SCREEN) }
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_back),
                            contentDescription = "ArrowBack",
                            tint = Color.White,
                        )
                    }
                },
            )
        },
    ) {
        Surface {
            Box(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentAlignment = Alignment.BottomEnd,
            ) {
                val state = rememberLazyListState()
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 12.dp, top = 8.dp, end = 12.dp, bottom = 64.dp),
                    state = state
                ) {
                    itemsIndexed(
                        items = textInputList,
                        // Provide a unique key based on the item content
                        key = { _, item -> item.hashCode() },
                        itemContent = { _, item ->
                            AnimatedVisibility(
                                visible = !deletedItems.contains(item),
                                enter = expandHorizontally(),
                                exit = shrinkHorizontally(animationSpec = tween(300))
                            ) {
                                val text = rememberSaveable { mutableStateOf(item.text) }
                                val isSelected = rememberSaveable { mutableStateOf(item.isSelected) }
                                TextInputCard(
                                    text = text,
                                    isSelected = isSelected,
                                    onItemClick = {
                                        keyboardController?.hide()
                                        if (textInputList.any { it.isSelected }) {
                                            toggleSelectedItem(
                                                item = item,
                                                isSelected = isSelected,
                                                showFABs = showFABs,
                                                textInputList = textInputList,
                                                deletedItems = deletedItems,
                                            )
                                        }
                                    },
                                    onLongPress = {
                                        toggleSelectedItem(
                                            item = item,
                                            isSelected = isSelected,
                                            showFABs = showFABs,
                                            textInputList = textInputList,
                                            deletedItems = deletedItems,
                                        )
                                    },
                                    onTextFieldFocused = {
                                        if (it.isFocused && item.isSelected)
                                            toggleSelectedItem(
                                                item = item,
                                                isSelected = isSelected,
                                                showFABs = showFABs,
                                                textInputList = textInputList,
                                                deletedItems = deletedItems,
                                            )
                                    })
                                item.text = text.value
                            }
                        }
                    )
                }
                if (showFABs.value) {
                    FloatingDeleteExtendedButton(
                        {
                            textInputList = textInputList.drop(textInputList.size)
                            showFABs.value = false
                        },
                        { deleteSelected(textInputList, deletedItems, showFABs) }
                    )
                }
                Button(
                    onClick = {
                        val dataList = textInputList.filterNot { deletedItems.contains(it) }
                        resultText.value = collectedInputs(dataList, context)
                        openDialog.value = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp)
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                ) {
                    Text(
                        text = stringResource(R.string.button_collect_input_text),
                        style = MaterialTheme.typography.titleLarge,
                    )
                }
            }
            if (openDialog.value) ShowResultDialog(
                result = resultText.value,
                dialogState = openDialog
            )
        }
    }
}

private fun toggleSelectedItem(
    item: TextItemData,
    isSelected: MutableState<Boolean>,
    showFABs: MutableState<Boolean>,
    textInputList: List<TextItemData>,
    deletedItems: SnapshotStateList<TextItemData>
) {
    isSelected.value = !isSelected.value
    item.isSelected = isSelected.value
    showFABs.value = (textInputList.filterNot {
        deletedItems.contains(it)
    }.any { it.isSelected })
}

@Composable
fun FloatingDeleteExtendedButton(
    onDeleteAllClick: () -> Unit,
    onDeleteSelectedClick: () -> Unit,
    isExpand: MutableState<Boolean> = rememberSaveable { mutableStateOf(false) }
) {
    Column(horizontalAlignment = Alignment.End) {
        if (isExpand.value) {
            DeletionFABs(onDeleteAllClick, onDeleteSelectedClick)
        }
        ExtendedFloatingActionButton(
            modifier = Modifier.padding(end = 12.dp, bottom = 80.dp),
            text = { Text(stringResource(R.string.delete_actions)) },
            icon = { Icon(Icons.Filled.Delete, contentDescription = "Delete") },
            containerColor = MaterialTheme.colorScheme.tertiary,
            contentColor = MaterialTheme.colorScheme.background,
            onClick = { isExpand.value = !isExpand.value },
            expanded = isExpand.value,
            shape = CircleShape
        )
    }
}

@Composable
fun DeletionFABs(onDeleteAllClick: () -> Unit, onDeleteSelectedClick: () -> Unit) {
    Column(
        modifier = Modifier.padding(end = 12.dp),
        horizontalAlignment = Alignment.End
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier.padding(end = 6.dp),
                text = stringResource(R.string.delete_selected),
                color = MaterialTheme.colorScheme.onBackground,
            )
            SmallFloatingActionButton(
                onClick = onDeleteSelectedClick,
                containerColor = MaterialTheme.colorScheme.tertiary,
                contentColor = MaterialTheme.colorScheme.background,
                shape = CircleShape
            ) {
                Icon(Icons.Filled.Delete, contentDescription = "Delete selected")
            }
        }
        Row(
            modifier = Modifier.padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier.padding(end = 6.dp),
                text = stringResource(R.string.delete_all),
                color = MaterialTheme.colorScheme.onBackground,
            )
            SmallFloatingActionButton(
                onClick = onDeleteAllClick,
                containerColor = MaterialTheme.colorScheme.tertiary,
                contentColor = MaterialTheme.colorScheme.background,
                shape = CircleShape
            ) {
                Icon(Icons.Filled.Delete, contentDescription = "Delete selected")
            }
        }
    }
}

private fun deleteSelected(
    textInputList: List<TextItemData>,
    deletedItems: SnapshotStateList<TextItemData>,
    showFABs: MutableState<Boolean>
) {
    val selectedItems = textInputList.filter { it.isSelected }
    deletedItems.addAll(selectedItems)
    showFABs.value = false
}

@Preview(
    showBackground = true,
    widthDp = 300,
    heightDp = 400,
    uiMode = UI_MODE_NIGHT_YES,
    name = "nightMode"
)
@Preview(showBackground = true, widthDp = 300, heightDp = 400)
@Composable
fun FloatingDeleteExtendedButtonPreview() {
    ComposePracticeTheme {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomEnd,
        ) {
            FloatingDeleteExtendedButton({}, {}, rememberSaveable { mutableStateOf(true) })
        }
    }
}

private fun collectedInputs(inputs: List<TextItemData>, context: Context): String {
    if (inputs.all { it.text.isBlank() }) return context.getString(R.string.empty_data)
    var collectedText = ""
    inputs.onEachIndexed { index, textItemData ->
        collectedText += "${index + 1}. ${textItemData.text.ifBlank { context.getString(R.string.text_empty) }}\n"
    }
    return collectedText
}

@Composable
fun ShowResultDialog(result: String, dialogState: MutableState<Boolean>) {
    AlertDialog(
        onDismissRequest = { dialogState.value = false },
        confirmButton = {
            TextButton(onClick = {
                dialogState.value = false
            }) { Text(text = stringResource(R.string.button_ok_label)) }
        },
        dismissButton = {
            TextButton(onClick = {
                dialogState.value = false
            }) {
                Text(
                    text = stringResource(R.string.button_cancel_label),
                    color = MaterialTheme.colorScheme.onTertiary,
                )
            }
        },
        title = {
            Text(
                text = stringResource(R.string.result_alert_title),
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleMedium,
            )
        },
        text = {
            Text(
                text = result,
                color = MaterialTheme.colorScheme.onSurface,
            )
        },
        backgroundColor = MaterialTheme.colorScheme.background,
    )
}

@Preview(
    showBackground = true,
    widthDp = 480,
    heightDp = 600,
    uiMode = UI_MODE_NIGHT_YES,
    name = "nightMode"
)
@Preview(showBackground = true, widthDp = 480, heightDp = 600)
@Composable
fun ResultAlertPreview() {
    ComposePracticeTheme {
        ShowResultDialog(
            result = "Данные отсутствуют",
            dialogState = remember {
                mutableStateOf(true)
            })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextInputCard(
    text: MutableState<String>,
    isSelected: MutableState<Boolean>,
    onItemClick: (Offset) -> Unit,
    onLongPress: (Offset) -> Unit,
    onTextFieldFocused: (FocusState) -> Unit,
) {
    val cardBackground =
        if (isSelected.value) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.background
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = onLongPress,
                    onTap = onItemClick,
                )
            },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(cardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
    ) {
        OutlinedTextField(
            value = text.value,
            onValueChange = { newText ->
                text.value = newText.take(28)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .onFocusChanged(onTextFieldFocused),
            textStyle = TextStyle(fontSize = 16.sp),
            placeholder = { Text(stringResource(R.string.text_input_hint)) },
            singleLine = true,
            maxLines = 1,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
            )
        )
    }
}