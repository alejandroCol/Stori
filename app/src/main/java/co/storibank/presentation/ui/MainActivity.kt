package co.storibank.presentation.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import co.storibank.presentation.ui.screens.util.Stori
import co.storibank.presentation.viewmodel.HomeViewModel
import co.storibank.presentation.viewmodel.LoginViewModel
import co.storibank.presentation.viewmodel.RegistrationViewModel
import co.storibank.ui.theme.StoriBankTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val loginViewModel: LoginViewModel by viewModels()
    private val registrationViewModel: RegistrationViewModel by viewModels()
    private val homeViewModel: HomeViewModel by viewModels()

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StoriBankTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    Stori(
                        loginViewModel = loginViewModel,
                        registrationViewModel = registrationViewModel,
                        homeViewModel = homeViewModel,
                    )
                }
            }
        }
    }
}
