package co.storibank.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.storibank.R
import co.storibank.domain.model.User
import co.storibank.domain.usecase.RegisterUserUseCase
import co.storibank.domain.usecase.UploadImageUseCase
import co.storibank.presentation.viewmodel.state.RegistrationState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
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
        @ApplicationContext private val context: Context,
    ) : ViewModel() {
        private val _registrationState = MutableStateFlow<RegistrationState>(RegistrationState.Idle)
        val registrationState: StateFlow<RegistrationState> = _registrationState

        fun signUp(
            name: String,
            email: String,
            password: String,
            imageBytes: ByteArray,
        ) {
            _registrationState.value = RegistrationState.Loading

            viewModelScope.launch {
                try {
                    if (imageBytes.isEmpty() || email.isEmpty() || password.isEmpty()) {
                        val fieldsError = context.getString(R.string.fields_error)
                        handleError(fieldsError)
                        return@launch
                    }

                    val userId = registerUserUseCase(name, email, password)
                    uploadImageUseCase(User(name = name, email = email, uid = userId), imageBytes)
                    _registrationState.value = RegistrationState.Success
                } catch (e: Exception) {
                    val errorMessage = e.message ?: context.getString(R.string.error_general)
                    handleError(errorMessage)
                }
            }
        }

        private fun handleError(errorMessage: String) {
            _registrationState.value = RegistrationState.Error(errorMessage)
        }
    }
