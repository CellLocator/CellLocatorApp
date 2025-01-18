package com.celllocator.app

import android.content.Intent
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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.celllocator.app.ui.composables.ApplicationBar
import com.celllocator.app.ui.composables.CLNavigationBar
import com.celllocator.app.ui.composables.LoadingSpinner
import com.celllocator.app.ui.composables.PermissionRequired
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
                    ApplicationBar(scrollBehavior)
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
                    CLNavigationBar(navItems, navController)
                },
            )
        } else {
            Scaffold(
                modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                topBar = {
                    ApplicationBar(scrollBehavior)
                },
                content = { padding ->
                    PermissionRequired(padding, context)
                }
            )
        }
    }
}

