package com.settasit.onepen

import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun MainNavigation() {
    val navController = rememberNavController()
    val modelStateOne = remember { mutableIntStateOf(value = 0) }
    val modelStateTwo = remember { mutableIntStateOf(value = 0) }
    NavHost(
        navController = navController,
        startDestination = "Activity 1"
    ) {
        composable(route = "Activity 1") {
            Activity_1(navController = navController)
        }
        composable(route = "Activity 2") {
            Activity_2(
                navController = navController,
                modelStateOne = modelStateOne,
                modelStateTwo = modelStateTwo
            )
        }
        composable(route = "Activity 3") {
            Activity_3(
                modelValueOne = modelStateOne.intValue,
                modelValueTwo = modelStateTwo.intValue
            )
        }
    }
}