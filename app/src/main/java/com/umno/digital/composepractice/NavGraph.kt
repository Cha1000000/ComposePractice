package com.umno.digital.composepractice

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.umno.digital.composepractice.task1.ListOfTextInputsScreen
import com.umno.digital.composepractice.task2.ListOfUuidsScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    paddingValues: PaddingValues,
) {
    NavHost(
        navController = navController,
        startDestination = MAIN_SCREEN
    ) {
        composable(MAIN_SCREEN) {
            OnboardingScreen(navController)
        }
        composable(TASK_1_SCREEN) {
            ListOfTextInputsScreen(paddingValues, navController)
        }
        composable(TASK_2_SCREEN) {
            ListOfUuidsScreen(paddingValues, navController)
        }
    }
}