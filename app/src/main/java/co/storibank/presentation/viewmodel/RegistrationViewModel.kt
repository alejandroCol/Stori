package co.storibank.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.storibank.domain.usecase.RegisterUserUseCase
import co.storibank.domain.usecase.UploadImageUseCase
import co.storibank.presentation.viewmodel.state.RegistrationState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel
    @Inject
    constructor(
        private val registerUserUseCase: RegisterUserUseCase,
        private val uploadImageUseCase: UploadImageUseCase,
    ) : ViewModel() {
        private val _registrationState =
            MutableStateFlow<RegistrationState>(
                RegistrationState.Idle,
            )
        val registrationState: StateFlow<RegistrationState> = _registrationState

        fun signUp(
            email: String,
            password: String,
            imageBytes: ByteArray,
        ) {
            _registrationState.value = RegistrationState.Loading

            viewModelScope.launch {
                try {
                    val userId = registerUserUseCase(email, password)
                    uploadImageUseCase(userId, imageBytes)
                    _registrationState.value = RegistrationState.Success
                } catch (e: Exception) {
                    _registrationState.value =
                        RegistrationState.Error(
                            e.message ?: "An error occurred",
                        )
                }
            }
        }
    }
