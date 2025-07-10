package com.example.keygencetestapp

import PoDetailsScreen
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import androidx.room.Room
import com.example.keygencetestapp.constant.Routes
import com.example.keygencetestapp.database.StockDatabase
import com.example.keygencetestapp.database.migration.MIGRATION_1_2
import com.example.keygencetestapp.pages.HistoryPage.HistoryPage
import com.example.keygencetestapp.pages.HomePage.HomePage
import com.example.keygencetestapp.pages.OCRPage.CameraScreen
import com.example.keygencetestapp.pages.oneCheckScanPage.InputPage
import com.example.keygencetestapp.pages.stockInPage.StockInPage
import com.example.keygencetestapp.pages.stockInPage.components.NewItemScreen
import com.example.keygencetestapp.pages.stockInPage.components.QrScanning
import com.example.keygencetestapp.pages.stockOutPage.StockOutPage
import com.example.keygencetestapp.pages.stockOutPage.component.CreateStockOutPOForm
import com.example.keygencetestapp.ui.theme.KeyGenceTestAppTheme
import dagger.hilt.android.AndroidEntryPoint

import kotlinx.serialization.Serializable
import androidx.core.net.toUri
import com.example.keygencetestapp.pages.SettingPage.SettingPage
import com.example.keygencetestapp.pages.stockOutPage.StockOutTabs
import com.example.keygencetestapp.pages.stockOutPage.component.stockOutAction.StockOutActionScreen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    companion object {
        lateinit var DATABASE: StockDatabase
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DATABASE = Room.databaseBuilder(
            applicationContext,
            StockDatabase::class.java,
            StockDatabase.NAME
        ).addMigrations(MIGRATION_1_2).
        build()
        enableEdgeToEdge()

        setContent {
            val context = LocalContext.current
            KeyGenceTestAppTheme {
//                val ocrManager = OCRManager(LocalContext.current)
                val permissionLauncher = rememberLauncherForActivityResult(
                    ActivityResultContracts.RequestPermission()
                ) { granted ->
                    if (!granted) {
                        Toast.makeText(context, "Một vài tính năng sẽ bị lock", Toast.LENGTH_SHORT).show()
                    }
                }
//
                LaunchedEffect(Unit) {
                    permissionLauncher.launch(android.Manifest.permission.CAMERA)

                }
//                CameraScreen()


                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    floatingActionButton = {

                    }) { innerPadding ->
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Routes.HOME_SCREEN
                    ) {
                        composable(Routes.HOME_SCREEN) {
                            HomePage(
                                modifier = Modifier.padding(innerPadding),
                                navController = navController
                            )
                        }
                        composable(Routes.SETTING_SCREEN) {
                            SettingPage(
                                modifier = Modifier.padding(innerPadding)
                            )
                        }
                        composable(Routes.HISTORY_INVENTORY) {
                            HistoryPage(
                                modifier = Modifier.padding(innerPadding)
                            )
                        }
                        composable(Routes.STOCK_IN_QR_CODE_SCAN) {
                            QrScanning(navController = navController, modifier = Modifier.padding(innerPadding))
                        }
                        composable(Routes.STOCK_IN_OCR_SCAN) {
                            CameraScreen(navController = navController)
                        }
                        composable(Routes.ONE_CHECK_SCREEN) {
                            InputPage(modifier = Modifier.padding(innerPadding))
                        }
                        composable<CreateNewItemScreen> {
                            val args = it.toRoute<CreateNewItemScreen>()
                            NewItemScreen(itemCode = args.id, modifier = Modifier.padding(innerPadding), navController = navController)
                        }
                        composable<StockOutAction> {
                            val args = it.toRoute<PODetailScreen>()
                            StockOutActionScreen(args.id, modifier = Modifier.padding(innerPadding), navController = navController)
                        }
                        composable<PODetailScreen> {
                            val args = it.toRoute<PODetailScreen>()
                            PoDetailsScreen(navController, args.id, modifier = Modifier.padding(innerPadding))
                        }
                        composable(Routes.STOCK_IN_SCREEN) {
                            StockInPage(modifier = Modifier.padding(innerPadding), navController = navController)
                        }
                        composable(Routes.STOCK_OUT_SCREEN) {
                            StockOutPage(modifier = Modifier.padding(innerPadding), navController = navController)
                        }
                        composable<StockOutScreen> {
                            val args = it.toRoute<StockOutScreen>()
                            StockOutPage(modifier = Modifier.padding(innerPadding), navController = navController, args.tab)
                        }
                        composable(Routes.STOCK_OUT_CREATE_PO) {
                            CreateStockOutPOForm(modifier = Modifier.padding(innerPadding), navController = navController)
                        }
                    }
                }
            }
        }
    }
}

@Serializable
data class CreateNewItemScreen (
    val id: String
)

@Serializable
data class StockOutScreen (
    val tab: Int = StockOutTabs.DASHBOARD
)


@Serializable
data class PODetailScreen (
    val id: String
)

@Serializable
data class StockOutAction (
    val id: String
)

@Serializable
object OneCheckScreen