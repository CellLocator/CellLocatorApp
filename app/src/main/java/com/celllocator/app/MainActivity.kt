package com.celllocator.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CellTower
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.celllocator.app.ui.theme.CellLocatorTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent { MainActivityContent() }
    }
}

sealed class Screen(val route: String, @StringRes val resourceId: Int, val icon: ImageVector) {
    data object Cells : Screen("cellinfo", R.string.menu_cells, Icons.Rounded.CellTower)

    data object Settings : Screen("settings", R.string.menu_settings, Icons.Rounded.Settings)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainActivityContent() {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val navController = rememberNavController()

    val navItems = listOf(Screen.Cells, Screen.Settings)

    CellLocatorTheme {
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            stringResource(R.string.app_name),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    },
                    scrollBehavior = scrollBehavior,
                    colors =
                    topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                        scrolledContainerColor = MaterialTheme.colorScheme.primary,
                    ),
                )
            },
            content = { padding ->
                Surface(
                    modifier = Modifier.padding(padding),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Cells.route,
                    ) {
                        // placeholder for the screens
                        composable(Screen.Cells.route) {
                            Text("Info")
                        }
                        composable(Screen.Settings.route) {
                            Text("Settings")
                        }
                    }
                }
            },
            bottomBar = {
                NavigationBar {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination

                    navItems.forEach { screen ->
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    screen.icon,
                                    contentDescription = stringResource(id = screen.resourceId)
                                )
                            },
                            label = { Text(stringResource(id = screen.resourceId)) },
                            selected =
                            currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                            onClick = {
                                if (currentDestination?.route != screen.route) {
                                    navController.navigate(screen.route) {
                                        popUpTo(navController.graph.startDestinationId)
                                        launchSingleTop = true
                                    }
                                }
                            },
                        )
                    }
                }
            },
        )
    }
}