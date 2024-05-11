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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import co.storibank.R
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
                title = { Text(text = stringResource(R.string.login_title)) },
                modifier = Modifier.background(PurpleGrey80),
            )
        },
        content = {
            LoginContent(
                navigateToRegistration,
                navigateToHome,
                viewModel::signIn,
                loginState,
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

        EmailTextField(email) { email = it }
        Spacer(modifier = Modifier.height(16.dp))
        PasswordTextField(password) { password = it }
        Spacer(modifier = Modifier.height(16.dp))

        LoginButton(
            onClick = { onLogin(email, password) },
            enabled = loginState !is LoginState.Loading,
        )

        TextButton(onClick = navigateToRegistration, modifier = Modifier.fillMaxWidth()) {
            Text(text = stringResource(R.string.create_account))
        }

        when (loginState) {
            is LoginState.Success -> {
                SuccessMessage(stringResource(R.string.login_success), navigateToHome)
            }

            is LoginState.Error -> {
                ErrorMessage(stringResource(R.string.login_error, loginState.message))
            }

            else -> {
                Box(
                    modifier = Modifier.height(1.dp),
                )
            }
        }
    }
}

@Composable
private fun EmailTextField(
    email: String,
    onEmailChanged: (String) -> Unit,
) {
    OutlinedTextField(
        value = email,
        onValueChange = onEmailChanged,
        label = { Text(stringResource(R.string.email_label)) },
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
private fun PasswordTextField(
    password: String,
    onPasswordChanged: (String) -> Unit,
) {
    OutlinedTextField(
        value = password,
        onValueChange = onPasswordChanged,
        label = { Text(stringResource(R.string.password_label)) },
        modifier = Modifier.fillMaxWidth(),
        visualTransformation = PasswordVisualTransformation(),
    )
}

@Composable
private fun LoginButton(
    onClick: () -> Unit,
    enabled: Boolean,
) {
    Button(onClick = onClick, modifier = Modifier.fillMaxWidth(), enabled = enabled) {
        if (enabled) {
            Text(text = stringResource(R.string.sign_in))
        } else {
            CircularProgressIndicator()
        }
    }
}

@Composable
private fun SuccessMessage(
    message: String,
    action: () -> Unit,
) {
    Text(
        text = message,
        color = Color.Green,
        modifier = Modifier.padding(top = 16.dp),
    )
    action()
}

@Composable
private fun ErrorMessage(message: String) {
    Text(
        text = message,
        color = Color.Red,
        modifier = Modifier.padding(top = 16.dp),
    )
}
