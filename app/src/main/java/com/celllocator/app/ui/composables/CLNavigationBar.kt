package com.celllocator.app.ui.composables

import com.celllocator.app.util.Screen
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun CLNavigationBar(navItems: List<Screen>, navController: NavController) {
    androidx.compose.material3.NavigationBar {
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
}