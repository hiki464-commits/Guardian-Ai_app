package com.example.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.ui.screens.AiAnalysisScreen
import com.example.ui.screens.AmbulanceSelectionScreen
import com.example.ui.screens.AutoPaySetupScreen
import com.example.ui.screens.CostEstimationScreen
import com.example.ui.screens.CountdownScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.EmergencyContactsScreen
import com.example.ui.screens.HospitalSelectionScreen
import com.example.ui.screens.IncidentReportScreen
import com.example.ui.screens.JourneyMonitoringScreen
import com.example.ui.screens.LiveTrackingScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.NotificationsScreen
import com.example.ui.screens.OnboardingScreen
import com.example.ui.screens.PaymentHistoryScreen
import com.example.ui.screens.PaymentSuccessScreen
import com.example.ui.screens.PravaPaymentScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.SandboxWalletScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.SignUpScreen
import com.example.ui.screens.SplashScreen
import com.example.viewmodel.EmergencyViewModel

object Routes {
    const val SPLASH = "splash"
    const val ONBOARDING = "onboarding"
    const val LOGIN = "login"
    const val SIGN_UP = "sign_up"
    const val DASHBOARD = "dashboard"
    const val PROFILE = "profile"
    const val AUTOPAY_SETUP = "autopay_setup"
    const val JOURNEY_MONITORING = "journey_monitoring"
    const val AI_ANALYSIS = "ai_analysis"
    const val COUNTDOWN = "countdown"
    const val HOSPITAL_SELECTION = "hospital_selection"
    const val AMBULANCE_SELECTION = "ambulance_selection"
    const val COST_ESTIMATION = "cost_estimation"
    const val PRAVA_PAYMENT = "prava_payment"
    const val PAYMENT_SUCCESS = "payment_success"
    const val LIVE_TRACKING = "live_tracking"
    const val EMERGENCY_CONTACTS = "emergency_contacts"
    const val NOTIFICATIONS = "notifications"
    const val INCIDENT_REPORT = "incident_report"
    const val PAYMENT_HISTORY = "payment_history"
    const val SANDBOX_WALLET = "sandbox_wallet"
    const val SETTINGS = "settings"
}

@Composable
fun GuardianNavGraph(
    navController: NavHostController,
    viewModel: EmergencyViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH
    ) {
        composable(Routes.SPLASH) {
            SplashScreen(
                onSplashFinished = {
                    navController.navigate(Routes.ONBOARDING) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.ONBOARDING) {
            OnboardingScreen(
                onFinishOnboarding = {
                    navController.navigate(Routes.DASHBOARD) {
                        popUpTo(Routes.ONBOARDING) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Routes.DASHBOARD) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onNavigateToSignUp = { navController.navigate(Routes.SIGN_UP) }
            )
        }

        composable(Routes.SIGN_UP) {
            SignUpScreen(
                onSignUpSuccess = {
                    navController.navigate(Routes.DASHBOARD) {
                        popUpTo(Routes.SIGN_UP) { inclusive = true }
                    }
                },
                onNavigateToLogin = { navController.navigate(Routes.LOGIN) }
            )
        }

        composable(Routes.DASHBOARD) {
            DashboardScreen(
                viewModel = viewModel,
                onStartJourney = {
                    viewModel.startJourney()
                    navController.navigate(Routes.JOURNEY_MONITORING)
                },
                onNavigateToContacts = { navController.navigate(Routes.EMERGENCY_CONTACTS) },
                onNavigateToAutoPay = { navController.navigate(Routes.AUTOPAY_SETUP) },
                onNavigateToIncidents = { navController.navigate(Routes.INCIDENT_REPORT) },
                onNavigateToProfile = { navController.navigate(Routes.PROFILE) }
            )
        }

        composable(Routes.PROFILE) {
            ProfileScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onNavigateToAutoPay = { navController.navigate(Routes.AUTOPAY_SETUP) },
                onNavigateToContacts = { navController.navigate(Routes.EMERGENCY_CONTACTS) },
                onNavigateToHistory = { navController.navigate(Routes.PAYMENT_HISTORY) },
                onNavigateToWallet = { navController.navigate(Routes.SANDBOX_WALLET) }
            )
        }

        composable(Routes.AUTOPAY_SETUP) {
            AutoPaySetupScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.JOURNEY_MONITORING) {
            JourneyMonitoringScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onSimulateAccident = {
                    viewModel.triggerSimulatedAccident(
                        onAnalysisComplete = {
                            navController.navigate(Routes.AI_ANALYSIS)
                        }
                    )
                }
            )
        }

        composable(Routes.AI_ANALYSIS) {
            AiAnalysisScreen(
                viewModel = viewModel,
                onNavigateToCountdown = { navController.navigate(Routes.COUNTDOWN) }
            )
        }

        composable(Routes.COUNTDOWN) {
            CountdownScreen(
                viewModel = viewModel,
                onCountdownExpired = { navController.navigate(Routes.HOSPITAL_SELECTION) },
                onCancelled = { navController.navigate(Routes.DASHBOARD) }
            )
        }

        composable(Routes.HOSPITAL_SELECTION) {
            HospitalSelectionScreen(
                viewModel = viewModel,
                onHospitalSelected = { navController.navigate(Routes.AMBULANCE_SELECTION) }
            )
        }

        composable(Routes.AMBULANCE_SELECTION) {
            AmbulanceSelectionScreen(
                viewModel = viewModel,
                onAmbulanceSelected = { navController.navigate(Routes.COST_ESTIMATION) }
            )
        }

        composable(Routes.COST_ESTIMATION) {
            CostEstimationScreen(
                viewModel = viewModel,
                onProceedToPrava = { navController.navigate(Routes.PRAVA_PAYMENT) }
            )
        }

        composable(Routes.PRAVA_PAYMENT) {
            PravaPaymentScreen(
                viewModel = viewModel,
                onPaymentCompleted = { navController.navigate(Routes.PAYMENT_SUCCESS) }
            )
        }

        composable(Routes.PAYMENT_SUCCESS) {
            PaymentSuccessScreen(
                viewModel = viewModel,
                onTrackLive = { navController.navigate(Routes.LIVE_TRACKING) },
                onViewReport = { navController.navigate(Routes.INCIDENT_REPORT) }
            )
        }

        composable(Routes.LIVE_TRACKING) {
            LiveTrackingScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onViewIncidentReport = { navController.navigate(Routes.INCIDENT_REPORT) }
            )
        }

        composable(Routes.EMERGENCY_CONTACTS) {
            EmergencyContactsScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.NOTIFICATIONS) {
            NotificationsScreen(onBack = { navController.popBackStack() })
        }

        composable(Routes.INCIDENT_REPORT) {
            IncidentReportScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.PAYMENT_HISTORY) {
            PaymentHistoryScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.SANDBOX_WALLET) {
            SandboxWalletScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.SETTINGS) {
            SettingsScreen(onBack = { navController.popBackStack() })
        }
    }
}
