package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.ContactPhone
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ui.navigation.GuardianNavGraph
import com.example.ui.navigation.Routes
import com.example.ui.theme.GuardianAITheme
import com.example.ui.theme.GuardianPrimary
import com.example.ui.theme.GuardianTextMuted
import com.example.viewmodel.EmergencyViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: EmergencyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            GuardianAITheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                // Hide bottom bar during critical full screen flows (Splash, Onboarding, Countdown, Payment, Live Tracking)
                val isFullscreenFlow = currentRoute in listOf(
                    Routes.SPLASH,
                    Routes.ONBOARDING,
                    Routes.LOGIN,
                    Routes.SIGN_UP,
                    Routes.COUNTDOWN,
                    Routes.PRAVA_PAYMENT,
                    Routes.PAYMENT_SUCCESS,
                    Routes.LIVE_TRACKING
                )

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        if (!isFullscreenFlow && currentRoute != null) {
                            NavigationBar(
                                containerColor = androidx.compose.material3.MaterialTheme.colorScheme.surfaceVariant,
                                tonalElevation = 8.dp
                            ) {
                                NavigationBarItem(
                                    selected = currentRoute == Routes.DASHBOARD,
                                    onClick = {
                                        navController.navigate(Routes.DASHBOARD) {
                                            popUpTo(Routes.DASHBOARD) { inclusive = true }
                                        }
                                    },
                                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                                    label = { Text("Home", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = GuardianPrimary,
                                        selectedTextColor = GuardianPrimary,
                                        unselectedIconColor = GuardianTextMuted,
                                        unselectedTextColor = GuardianTextMuted
                                    )
                                )

                                NavigationBarItem(
                                    selected = currentRoute == Routes.JOURNEY_MONITORING,
                                    onClick = {
                                        viewModel.startJourney()
                                        navController.navigate(Routes.JOURNEY_MONITORING)
                                    },
                                    icon = { Icon(Icons.Default.Navigation, contentDescription = "Guard") },
                                    label = { Text("Guard", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = GuardianPrimary,
                                        selectedTextColor = GuardianPrimary,
                                        unselectedIconColor = GuardianTextMuted,
                                        unselectedTextColor = GuardianTextMuted
                                    )
                                )

                                NavigationBarItem(
                                    selected = currentRoute == Routes.EMERGENCY_CONTACTS,
                                    onClick = { navController.navigate(Routes.EMERGENCY_CONTACTS) },
                                    icon = { Icon(Icons.Default.ContactPhone, contentDescription = "Contacts") },
                                    label = { Text("Contacts", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = GuardianPrimary,
                                        selectedTextColor = GuardianPrimary,
                                        unselectedIconColor = GuardianTextMuted,
                                        unselectedTextColor = GuardianTextMuted
                                    )
                                )

                                NavigationBarItem(
                                    selected = currentRoute == Routes.PAYMENT_HISTORY,
                                    onClick = { navController.navigate(Routes.PAYMENT_HISTORY) },
                                    icon = { Icon(Icons.Default.AccountBalance, contentDescription = "Escrow") },
                                    label = { Text("Prava", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = GuardianPrimary,
                                        selectedTextColor = GuardianPrimary,
                                        unselectedIconColor = GuardianTextMuted,
                                        unselectedTextColor = GuardianTextMuted
                                    )
                                )

                                NavigationBarItem(
                                    selected = currentRoute == Routes.SETTINGS,
                                    onClick = { navController.navigate(Routes.SETTINGS) },
                                    icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                                    label = { Text("Settings", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = GuardianPrimary,
                                        selectedTextColor = GuardianPrimary,
                                        unselectedIconColor = GuardianTextMuted,
                                        unselectedTextColor = GuardianTextMuted
                                    )
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    androidx.compose.foundation.layout.Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        GuardianNavGraph(
                            navController = navController,
                            viewModel = viewModel
                        )
                    }
                }
            }
        }
    }
}
