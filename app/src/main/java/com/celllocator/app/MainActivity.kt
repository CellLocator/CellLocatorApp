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
import com.celllocator.app.entities.CellDetails
import com.celllocator.app.ui.composables.ApplicationBar
import com.celllocator.app.ui.composables.CLNavigationBar
import com.celllocator.app.ui.composables.CellDetailsScreen
import com.celllocator.app.ui.composables.LoadingSpinner
import com.celllocator.app.ui.composables.PermissionRequired
import com.celllocator.app.ui.theme.CellLocatorTheme
import com.celllocator.app.util.Screen
import com.celllocator.app.util.checkPermissions
import com.celllocator.app.util.requestPermissions

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
                                val cellDataList = listOf<CellDetails>()

                                // Example data for preview and testing
                                val cellDetailsList = listOf(
                                    CellDetails(
                                        networkType = "LTE",
                                        mcc = "262",
                                        mnc = "01",
                                        tac = "12345",
                                        rsrp = -85,
                                        signalStrength = 25,
                                        cellNr = 1001,
                                        sectorId = 1,
                                        rfcn = 1800,
                                        pci = 501,
                                        rsrq = -12,
                                        sinr = 20,
                                        bandNr = 3,
                                        bandName = "Band 3",
                                        rxFrequency = 1805.0,
                                        txFrequency = 1710.0
                                    ),
                                    CellDetails(
                                        networkType = "NR SA",
                                        mcc = "262",
                                        mnc = "02",
                                        tac = "23456",
                                        rsrp = -90,
                                        signalStrength = 20,
                                        cellNr = 1002,
                                        sectorId = 2,
                                        rfcn = 3500,
                                        pci = 502,
                                        rsrq = -10,
                                        sinr = 15,
                                        bandNr = 78,
                                        bandName = "n78",
                                        rxFrequency = 3500.0,
                                        txFrequency = 3400.0
                                    ),
                                    CellDetails(
                                        networkType = "LTE",
                                        mcc = "262",
                                        mnc = "03",
                                        tac = "34567",
                                        rsrp = -80,
                                        signalStrength = 30,
                                        cellNr = 1003,
                                        sectorId = 3,
                                        rfcn = 2600,
                                        pci = 503,
                                        rsrq = -8,
                                        sinr = 25,
                                        bandNr = 7,
                                        bandName = "Band 7",
                                        rxFrequency = 2620.0,
                                        txFrequency = 2520.0
                                    ),
                                    CellDetails(
                                        networkType = "UMTS",
                                        mcc = "262",
                                        mnc = "04",
                                        tac = "45678",
                                        rsrp = -95,
                                        signalStrength = 15,
                                        cellNr = 1004,
                                        sectorId = 4,
                                        rfcn = 2100,
                                        pci = 504,
                                        rsrq = -15,
                                        sinr = 10,
                                        bandNr = 1,
                                        bandName = "Band 1",
                                        rxFrequency = 2110.0,
                                        txFrequency = 1920.0
                                    ),
                                    CellDetails(
                                        networkType = "NR SA",
                                        mcc = "262",
                                        mnc = "05",
                                        tac = "56789",
                                        rsrp = -75,
                                        signalStrength = 35,
                                        cellNr = 1005,
                                        sectorId = 5,
                                        rfcn = 3700,
                                        pci = 505,
                                        rsrq = -7,
                                        sinr = 30,
                                        bandNr = 77,
                                        bandName = "n77",
                                        rxFrequency = 3700.0,
                                        txFrequency = 3600.0
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

