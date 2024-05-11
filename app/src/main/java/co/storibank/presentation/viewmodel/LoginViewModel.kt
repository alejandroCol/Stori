package co.storibank.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.storibank.R
import co.storibank.domain.usecase.LoginUseCase
import co.storibank.presentation.viewmodel.state.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel
    @Inject
    constructor(
        private val loginUseCase: LoginUseCase,
        @ApplicationContext private val context: Context,
    ) : ViewModel() {
        private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
        val loginState: StateFlow<LoginState> = _loginState

        fun signIn(
            email: String,
            password: String,
        ) {
            _loginState.value = LoginState.Loading

            viewModelScope.launch {
                try {
                    val isSuccess = loginUseCase(email, password)
                    handleLoginSuccess(isSuccess)
                } catch (e: Exception) {
                    handleLoginError(e)
                }
            }
        }

        private fun handleLoginSuccess(isSuccess: Boolean) {
            _loginState.value =
                if (isSuccess) {
                    LoginState.Success
                } else {
                    val errorMessage = context.getString(R.string.login_error_incorrect)
                    LoginState.Error(errorMessage)
                }
        }

        private fun handleLoginError(e: Exception) {
            val errorMessage = e.message ?: context.getString(R.string.login_error_default)
            _loginState.value = LoginState.Error(errorMessage)
        }
    }
