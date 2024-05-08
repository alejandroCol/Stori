package co.storibank.presentation.ui.screens.login

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import co.storibank.presentation.viewmodel.LoginViewModel
import co.storibank.presentation.viewmodel.state.LoginState
import co.storibank.ui.theme.PurpleGrey80

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    navigateToRegistration: () -> Unit,
    navigateToHome: () -> Unit,
) {
    val loginState by viewModel.loginState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Login") },
                modifier = Modifier.background(PurpleGrey80),
            )
        },
        content = {
            LoginContent(
                navigateToRegistration = navigateToRegistration,
                navigateToHome = navigateToHome,
                onLogin = { email, password ->
                    viewModel.signIn(email, password)
                },
                loginState = loginState,
            )
        },
    )
}

@Composable
fun LoginContent(
    navigateToRegistration: () -> Unit,
    navigateToHome: () -> Unit,
    onLogin: (String, String) -> Unit,
    loginState: LoginState,
) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onLogin(email, password) },
            modifier = Modifier.fillMaxWidth(),
            enabled = loginState !is LoginState.Loading,
        ) {
            if (loginState is LoginState.Loading) {
                CircularProgressIndicator()
            } else {
                Text(text = "Sign In")
            }
        }

        TextButton(
            onClick = { navigateToRegistration() },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(text = "Create an account")
        }

        if (loginState is LoginState.Success) {
            Text(
                text = "Login Successful",
                color = Color.Green,
                modifier = Modifier.padding(top = 16.dp),
            )
            navigateToHome()
        }

        if (loginState is LoginState.Error) {
            Text(
                text = "Login Error: ${loginState.message}",
                color = Color.Red,
                modifier = Modifier.padding(top = 16.dp),
            )
        }
    }
}
