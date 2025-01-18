package com.celllocator.app

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CellTower
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Button
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.celllocator.app.ui.composables.LoadingSpinner
import com.celllocator.app.ui.theme.CellLocatorTheme
import com.celllocator.app.util.checkAndRequestPermissions

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MainActivityContent(
                checkAndRequestPermissions = { checkAndRequestPermissions(this, this) }
            )
        }
    }

    override fun onResume() {
        super.onResume()
        val permissionsGranted = checkAndRequestPermissions(this, this)
        if (permissionsGranted) {
            setContent {
                MainActivityContent(
                    checkAndRequestPermissions = { checkAndRequestPermissions(this, this) }
                )
            }
        }
    }
}

sealed class Screen(val route: String, @StringRes val resourceId: Int, val icon: ImageVector) {
    data object Cells : Screen("cellinfo", R.string.menu_cells, Icons.Rounded.CellTower)

    data object Settings : Screen("settings", R.string.menu_settings, Icons.Rounded.Settings)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainActivityContent(checkAndRequestPermissions: () -> Boolean) {
    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val navController = rememberNavController()

    val isLoading = remember { mutableStateOf(true) }
    val navItems = listOf(Screen.Cells, Screen.Settings)
    var permissionsGranted by remember { mutableStateOf(false) }

    CellLocatorTheme {
        LaunchedEffect(Unit) {
            permissionsGranted = checkAndRequestPermissions()
        }

        LaunchedEffect(Unit) {
            isLoading.value = true
            permissionsGranted = checkAndRequestPermissions()
            isLoading.value = false
        }

        if (isLoading.value) {
            LoadingSpinner()
        } else if (permissionsGranted) {
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
                        colors = topAppBarColors(
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
        } else {
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
                        colors = topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            titleContentColor = MaterialTheme.colorScheme.onPrimary,
                            scrolledContainerColor = MaterialTheme.colorScheme.primary,
                        ),
                    )
                },
                content = { padding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(stringResource(R.string.permission_required))
                            Button(onClick = {
                                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                val uri = Uri.fromParts("package", context.packageName, null)
                                intent.data = uri
                                context.startActivity(intent)
                            }) {
                                Text(stringResource(R.string.open_settings))
                            }
                        }
                    }
                }
            )
        }
    }
}

