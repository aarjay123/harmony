package com.nugget.hios

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.navigation.compose.rememberNavController
import com.nugget.hios.bottomBar.BottomBar
import com.nugget.hios.bottomBar.SwipeableBottomBar
import com.nugget.hios.itemList.bottomNavItemList
import com.nugget.hios.navigation.BottomNavGraph
import com.nugget.hios.ui.theme.BottomBarTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BottomBarTheme {

                val navController = rememberNavController()
                BottomNavGraph(navController = navController)
                val pagerState = rememberPagerState(pageCount = { bottomNavItemList.size })

                Scaffold(
                    bottomBar = {
                        BottomBar(navController = navController, pagerState = pagerState)
                    }
                ) { paddingValues ->
                    SwipeableBottomBar(pagerState = pagerState, paddingValues = paddingValues, navController = navController)
                }
            }
        }
    }
}