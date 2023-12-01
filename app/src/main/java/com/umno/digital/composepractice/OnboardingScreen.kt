package com.umno.digital.composepractice

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.TopAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.umno.digital.composepractice.ui.theme.ComposePracticeTheme


@Composable
fun OnboardingScreen(navController: NavHostController) {
    Surface {
        Box {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.app_name),
                        color = Color.White,
                        fontSize = 20.sp
                    )
                },
                backgroundColor = MaterialTheme.colorScheme.primary,
            )
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = stringResource(R.string.select_task_text),
                    modifier = Modifier.padding(bottom = 16.dp),
                    fontSize = 28.sp,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    text = stringResource(R.string.task1_label_title),
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.titleMedium,
                )
                Button(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .width(280.dp)
                        .height(56.dp),
                    onClick = { navController.navigate(TASK_1_SCREEN) }) {
                    Text(
                        text = stringResource(R.string.button_task1_text),
                        fontSize = 16.sp,
                    )
                }
                Text(
                    text = stringResource(R.string.task2_label_title),
                    modifier = Modifier.padding(horizontal = 16.dp),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.titleMedium,
                )
                Button(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .width(280.dp)
                        .height(56.dp),
                    onClick = { navController.navigate(TASK_2_SCREEN) }) {
                    Text(
                        text = stringResource(R.string.button_task2_text),
                        fontSize = 16.sp,
                    )
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
    widthDp = 480,
    heightDp = 480,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "nightMode"
)
@Preview(showBackground = true, widthDp = 480, heightDp = 480)
@Composable
fun OnboardingScreenPreview() {
    ComposePracticeTheme {
        OnboardingScreen(rememberNavController())
    }
}
