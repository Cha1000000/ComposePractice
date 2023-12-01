package com.umno.digital.composepractice

import android.content.res.Configuration
import androidx.compose.foundation.layout.height
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Scaffold
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.umno.digital.composepractice.ui.theme.ComposePracticeTheme

sealed class BottomItem(val title: String, val iconId: Int, val route: String) {
    object HomeScreen : BottomItem("Home", R.drawable.ic_home, MAIN_SCREEN)
    object Task1Screen : BottomItem("Задание 1", R.drawable.ic_task, TASK_1_SCREEN)
    object Task2Screen : BottomItem("Задание 2", R.drawable.ic_task, TASK_2_SCREEN)
}

@Composable
fun BottomNavigation(navController: NavController) {
    val listItems = listOf(
        BottomItem.HomeScreen,
        BottomItem.Task1Screen,
        BottomItem.Task2Screen,
    )
    BottomAppBar(
        modifier = Modifier.height(54.dp),
        containerColor = MaterialTheme.colorScheme.primary
    ) {
        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = backStackEntry?.destination?.route
        listItems.forEach { item ->
            BottomNavigationItem(
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route)
                },
                icon = {
                    Icon(
                        painter = painterResource(item.iconId),
                        contentDescription = "Icon"
                    )
                },
                label = {
                    Text(
                        text = item.title,
                        fontSize = 10.sp
                    )
                },
                selectedContentColor = MaterialTheme.colorScheme.tertiary,
                unselectedContentColor = Color.Gray
            )
        }
    }
}

@Preview(
    showBackground = true,
    widthDp = 360,
    heightDp = 60,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "nightMode"
)
@Preview(showBackground = true, widthDp = 360, heightDp = 60)
@Composable
fun BottomNavigationPreview() {
    ComposePracticeTheme {
        BottomNavigation(rememberNavController())
    }
}

@Preview(
    showBackground = true,
    widthDp = 480,
    heightDp = 700,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "nightMode"
)
@Preview(showBackground = true, widthDp = 480, heightDp = 480)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    ComposePracticeTheme {
        Scaffold(
            bottomBar = {
                BottomNavigation(navController)
            }
        ) { paddings ->
            NavGraph(navController, paddings)
        }
    }
}