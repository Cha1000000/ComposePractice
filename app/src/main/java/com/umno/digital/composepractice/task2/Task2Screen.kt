package com.umno.digital.composepractice.task2

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.pullRefreshIndicatorTransform
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.umno.digital.composepractice.MAIN_SCREEN
import com.umno.digital.composepractice.R
import com.umno.digital.composepractice.common.DismissBackground
import com.umno.digital.composepractice.data.UuidItem
import com.umno.digital.composepractice.progress.ProgressIndicator
import com.umno.digital.composepractice.ui.theme.ComposePracticeTheme
import com.umno.digital.composepractice.ui.theme.Orange
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ListOfUuidsScreen(
    uuidItemsViewModel: UuidItemsViewModel = viewModel(),
    paddingValues: PaddingValues,
    navController: NavHostController,
) {
    // Collect the state of uuids from the view model
    val uuidList by uuidItemsViewModel.uuidsListState.collectAsState()
    val refreshScope = rememberCoroutineScope()
    var refreshing by remember { mutableStateOf(false) }

    fun refresh() = refreshScope.launch {
        refreshing = true
        delay(300)
        uuidItemsViewModel.refreshAll()
        refreshing = false
    }

    val refreshState = rememberPullRefreshState(refreshing, ::refresh)
    val rotation = animateFloatAsState(refreshState.progress * 300, label = "")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.task2_screen_title),
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
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.padding(bottom = 70.dp),
                onClick = { uuidItemsViewModel.addItem() },
                containerColor = Orange,
                contentColor = MaterialTheme.colorScheme.background,
                shape = CircleShape
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add item")
            }
        },
    ) {
        Surface {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(color = MaterialTheme.colorScheme.background)
                    .pullRefresh(refreshState),
                contentAlignment = Alignment.BottomEnd,
            ) {
                val state = rememberLazyListState()
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = state
                ) {
                    if (!refreshing) {
                        itemsIndexed(
                            items = uuidList,
                            // Provide a unique key based on the item content
                            key = { _, item -> item.hashCode() },
                            itemContent = { _, item ->
                                UUIDItemSwipeToDeleteWrapper(
                                    uuidItem = item,
                                    onUpdateUUID = { uuidItemsViewModel.refreshItem(item) },
                                    onRemove = { uuidItemsViewModel.removeItem(item) }
                                )
                            }
                        )
                    }
                }
                //PullRefreshIndicator(refreshing, refreshState, Modifier.align(Alignment.TopCenter))
                // Custom Refresh Indicator
                Surface(
                    modifier = Modifier
                        .size(50.dp)//40
                        .align(Alignment.TopCenter)
                        .pullRefreshIndicatorTransform(refreshState)
                        .rotate(rotation.value),
                    shape = RoundedCornerShape(50.dp),
                    color = MaterialTheme.colorScheme.onSecondary,
                    elevation = if (refreshState.progress > 0 || refreshing) 20.dp else 0.dp,
                ) {
                    Box {
                        if (refreshing) {
                            /*CircularProgressIndicator(
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .size(25.dp),
                                color = MaterialTheme.colorScheme.onBackground,
                                strokeWidth = 3.dp
                            )*/
                            ProgressIndicator(modifier = Modifier.padding(12.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun UUIDItem(item: UuidItem, onUpdateUUID: () -> Unit) {
    Column(
        modifier = Modifier
            .background(Color.Gray)
            .padding(vertical = 0.5.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(start = 20.dp, top = 6.dp, end = 8.dp, bottom = 6.dp),
            horizontalArrangement = Arrangement.Absolute.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = item.uuid, fontSize = 14.sp)
            IconButton(onClick = onUpdateUUID) {
                Icon(
                    imageVector = Icons.Filled.Refresh,
                    contentDescription = "Refresh",
                    tint = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}

@Preview(
    showBackground = true,
    widthDp = 480,
    heightDp = 100,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "nightMode"
)
@Preview(showBackground = true, widthDp = 480, heightDp = 100)
@Composable
fun ListItemPreview() {
    ComposePracticeTheme {
        Box(
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
        ) {
            UUIDItem(item = UuidItem(UUID.randomUUID().toString()), onUpdateUUID = {})
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun UUIDItemSwipeToDeleteWrapper(
    uuidItem: UuidItem,
    onUpdateUUID: () -> Unit,
    onRemove: () -> Unit
) {
    val context = LocalContext.current
    var show by remember { mutableStateOf(true) }
    val dismissState = rememberDismissState(
        confirmStateChange = {
            if (it == DismissValue.DismissedToStart/* || it == DismissValue.DismissedToEnd*/) {
                show = false
                true
            } else false
        }
    )
    AnimatedVisibility(
        show, exit = fadeOut(spring())
    ) {
        SwipeToDismiss(
            state = dismissState,
            modifier = Modifier,
            background = {
                DismissBackground(dismissState)
            },
            dismissContent = {
                UUIDItem(uuidItem, onUpdateUUID)
            }
        )
    }

    LaunchedEffect(show) {
        if (!show) {
            delay(800)
            onRemove()
            Toast.makeText(context, "Item removed", Toast.LENGTH_SHORT).show()
        }
    }
}