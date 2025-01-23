package com.nugget.hios.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.nugget.hios.screens.HomeScreen
import com.nugget.hios.screens.NotificationScreen
import com.nugget.hios.screens.ProfileScreen
import com.nugget.hios.screens.SearchScreen

@Composable
fun BottomNavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = ScreenName.Home.route
    ){
        composable(ScreenName.Home.route){
            HomeScreen(navController = navController)
        }

        composable(ScreenName.Search.route){
            SearchScreen(navController = navController)
        }

        composable(ScreenName.Notifications.route){
            NotificationScreen(navController = navController)
        }

        composable(ScreenName.Profile.route){
            ProfileScreen(navController = navController)
        }
    }
}