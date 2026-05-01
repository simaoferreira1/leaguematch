package com.leaguematch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import com.leaguematch.funcionalidades.administrador.ecra.HomeScreen
import com.leaguematch.funcionalidades.autenticacao.ecra.LoginScreen
import com.leaguematch.ui.theme.LeagueMatchTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LeagueMatchTheme {
                var isLoggedIn by remember { mutableStateOf(false) }

                if (!isLoggedIn) {
                    LoginScreen(onLoginClick = { isLoggedIn = true })
                } else {
                    HomeScreen()
                }
            }
        }
    }
}
