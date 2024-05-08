package co.storibank.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.storibank.domain.usecase.LoginUseCase
import co.storibank.presentation.viewmodel.state.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel
    @Inject
    constructor(
        private val loginUseCase: LoginUseCase,
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
                    _loginState.value =
                        if (isSuccess) {
                            LoginState.Success
                        } else {
                            LoginState.Error("User or password incorrect")
                        }
                } catch (e: Exception) {
                    _loginState.value = LoginState.Error(e.message ?: "An error occurred")
                }
            }
        }
    }
