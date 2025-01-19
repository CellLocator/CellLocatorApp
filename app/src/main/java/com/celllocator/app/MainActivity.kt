package com.celllocator.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.celllocator.app.entities.Cell
import com.celllocator.app.ui.composables.ApplicationBar
import com.celllocator.app.ui.composables.CLNavigationBar
import com.celllocator.app.ui.composables.CellDetailsScreen
import com.celllocator.app.ui.composables.LoadingSpinner
import com.celllocator.app.ui.composables.PermissionRequired
import com.celllocator.app.ui.theme.CellLocatorTheme
import com.celllocator.app.util.Screen
import com.celllocator.app.util.checkPermissions
import com.celllocator.app.util.requestPermissions
import cz.mroczis.netmonster.core.model.connection.PrimaryConnection

class MainActivity : ComponentActivity() {

    private var permissionsGranted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        permissionsGranted = checkPermissions(this)
        setContent {
            MainActivityContent(permissionsGranted)
        }
    }

    override fun onResume() {
        super.onResume()

        val currentPermissionsGranted = checkPermissions(this)

        if (currentPermissionsGranted != permissionsGranted) {
            permissionsGranted = currentPermissionsGranted

            runOnUiThread {
                setContent {
                    MainActivityContent(permissionsGranted)
                }
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainActivityContent(permissionsGranted: Boolean) {
    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val navController = rememberNavController()

    val isLoading = remember { mutableStateOf(false) }
    val navItems = listOf(Screen.Cells, Screen.Settings)

    LaunchedEffect(permissionsGranted) {
        if (!permissionsGranted) {
            requestPermissions(context as MainActivity)
        }
    }

    CellLocatorTheme {
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
                                val cellDataList = listOf<Cell>()

                                // Example data for preview and testing
                                val cellDetailsList = listOf(
                                    Cell(
                                        networkType = "LTE",
                                        mcc = "262",
                                        mnc = "01",
                                        tac = "12345",
                                        rsrp = -85,
                                        signalStrength = 25,
                                        cellId = 1001,
                                        sectorId = 1,
                                        rfcn = 1800,
                                        pci = 501,
                                        rsrq = -12,
                                        sinr = 20,
                                        bandNr = 3,
                                        bandName = "Band 3",
                                        rxFrequency = 1805.0,
                                        txFrequency = 1710.0,
                                        connectionType = PrimaryConnection()
                                    )
                                )
                                CellDetailsScreen(cellDetailsList)
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

