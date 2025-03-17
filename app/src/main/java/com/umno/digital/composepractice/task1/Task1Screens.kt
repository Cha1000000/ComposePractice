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
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.AlertDialog
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Scaffold
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
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
import androidx.compose.foundation.background
import androidx.compose.material3.AlertDialogDefaults

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
            navController = rememberNavController(),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListOfTextInputsScreen(
    navController: NavHostController,
) {
    var textInputList by rememberSaveable { mutableStateOf(createTextInputList()) }
    val deletedItems = remember { mutableStateListOf<TextItemData>() }
    val openDialog = rememberSaveable { mutableStateOf(false) }
    val resultText = rememberSaveable { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current
    
    // Состояние выбранных элементов
    val selectedItems = remember { mutableStateListOf<TextItemData>() }

    Scaffold(
        topBar = {
            TextInputTopAppBar(
                onNavigateBack = { navController.navigate(MAIN_SCREEN) }
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.BottomEnd,
        ) {
            TextInputList(
                textInputList = textInputList,
                deletedItems = deletedItems,
                selectedItems = selectedItems,
                keyboardController = keyboardController,
            )
            
            if (selectedItems.isNotEmpty()) {
                FloatingDeleteExtendedButton(
                    onDeleteAllClick = {
                        textInputList = textInputList.drop(textInputList.size)
                        selectedItems.clear()
                    },
                    onDeleteSelectedClick = { 
                        deletedItems.addAll(selectedItems)
                        selectedItems.clear()
                        textInputList = ArrayList(textInputList)
                    }
                )
            }
            
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.05f))
                    .padding(bottom = 56.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                CollectInputButton(
                    onClick = {
                        val dataList = textInputList.filterNot { deletedItems.contains(it) }
                        resultText.value = collectedInputs(dataList, context)
                        openDialog.value = true
                    }
                )
            }
        }
        
        if (openDialog.value) {
            ShowResultDialog(
                result = resultText.value,
                dialogState = openDialog
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TextInputTopAppBar(onNavigateBack: () -> Unit) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(R.string.task1_screen_title),
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 20.sp
            )
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
        ),
        navigationIcon = {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "ArrowBack",
                    tint = MaterialTheme.colorScheme.onPrimary,
                )
            }
        },
    )
}

@Composable
private fun TextInputList(
    textInputList: List<TextItemData>,
    deletedItems: SnapshotStateList<TextItemData>,
    selectedItems: SnapshotStateList<TextItemData>,
    keyboardController: androidx.compose.ui.platform.SoftwareKeyboardController?,
) {
    val state = rememberLazyListState()
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.8f))
            .padding(start = 12.dp, top = 8.dp, end = 12.dp, bottom = 130.dp),
        state = state
    ) {
        itemsIndexed(
            items = textInputList,
            key = { _, item -> item.hashCode() },
            itemContent = { _, item ->
                AnimatedVisibility(
                    visible = !deletedItems.contains(item),
                    enter = expandHorizontally(),
                    exit = shrinkHorizontally(animationSpec = tween(300))
                ) {
                    val text = rememberSaveable { mutableStateOf(item.text) }
                    
                    TextInputCard(
                        item = item,
                        text = text,
                        isSelected = selectedItems.contains(item),
                        onItemClick = {
                            keyboardController?.hide()
                            // Если есть другие выделенные элементы, разрешаем короткое нажатие
                            if (selectedItems.isNotEmpty()) {
                                if (selectedItems.contains(item)) {
                                    selectedItems.remove(item)
                                } else {
                                    selectedItems.add(item)
                                }
                            }
                        },
                        onLongPress = {
                            if (selectedItems.contains(item)) {
                                selectedItems.remove(item)
                            } else {
                                selectedItems.add(item)
                            }
                        }
                    )
                }
            }
        )
    }
}

@Composable
private fun CollectInputButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
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
            modifier = Modifier.padding(end = 12.dp, bottom = 140.dp),
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
        modifier = Modifier.padding(end = 12.dp, bottom = 12.dp),
        horizontalAlignment = Alignment.End
    ) {
        DeleteActionRow(
            text = stringResource(R.string.delete_selected),
            onClick = onDeleteSelectedClick
        )
        
        DeleteActionRow(
            text = stringResource(R.string.delete_all),
            onClick = onDeleteAllClick,
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }
}

@Composable
private fun DeleteActionRow(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.padding(end = 6.dp),
            text = text,
            color = MaterialTheme.colorScheme.onBackground,
        )
        SmallFloatingActionButton(
            onClick = onClick,
            containerColor = MaterialTheme.colorScheme.tertiary,
            contentColor = MaterialTheme.colorScheme.background,
            shape = CircleShape
        ) {
            Icon(Icons.Filled.Delete, contentDescription = "Delete action")
        }
    }
}

private fun deleteSelected(
    textInputList: List<TextItemData>,
    deletedItems: SnapshotStateList<TextItemData>
) {
    val selectedItems = textInputList.filter { it.isSelected }
    deletedItems.addAll(selectedItems)
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
            FloatingDeleteExtendedButton({}, {})
        }
    }
}

private fun collectedInputs(inputs: List<TextItemData>, context: Context): String {
    if (inputs.all { it.text.isBlank() }) return context.getString(R.string.empty_data)
    
    return inputs.mapIndexed { index, textItemData ->
        "${index + 1}. ${textItemData.text.ifBlank { context.getString(R.string.text_empty) }}"
    }.joinToString("\n")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowResultDialog(result: String, dialogState: MutableState<Boolean>) {
    AlertDialog(
        onDismissRequest = { dialogState.value = false },
        confirmButton = {
            TextButton(onClick = { dialogState.value = false }) { 
                Text(
                    text = stringResource(R.string.button_ok_label),
                    color = MaterialTheme.colorScheme.onSecondary
                ) 
            }
        },
        dismissButton = {
            TextButton(onClick = { dialogState.value = false }) {
                Text(
                    text = stringResource(R.string.button_cancel_label),
                    color = MaterialTheme.colorScheme.onSecondary
                )
            }
        },
        title = {
            Text(
                text = stringResource(R.string.result_alert_title),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.titleMedium,
            )
        },
        text = {
            Text(
                text = result,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        },
        shape = MaterialTheme.shapes.extraLarge,
        backgroundColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
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
            dialogState = remember { mutableStateOf(true) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextInputCard(
    item: TextItemData,
    text: MutableState<String>,
    isSelected: Boolean,
    onItemClick: (Offset) -> Unit,
    onLongPress: (Offset) -> Unit,
) {
    val cardBackground =
        if (isSelected) MaterialTheme.colorScheme.scrim else MaterialTheme.colorScheme.background
    
    val textColor =
        if (isSelected) MaterialTheme.colorScheme.outlineVariant else MaterialTheme.colorScheme.onSurface
    
    val borderColor =
        if (isSelected) MaterialTheme.colorScheme.outlineVariant else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
    
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
                item.text = text.value
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            textStyle = TextStyle(
                fontSize = 16.sp,
                color = textColor
            ),
            placeholder = { 
                Text(
                    text = stringResource(R.string.text_input_hint),
                    color = textColor.copy(alpha = 0.7f)
                ) 
            },
            singleLine = true,
            maxLines = 1,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = if (isSelected) MaterialTheme.colorScheme.outlineVariant else MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = borderColor,
                focusedTextColor = textColor,
                unfocusedTextColor = textColor,
            )
        )
    }
}