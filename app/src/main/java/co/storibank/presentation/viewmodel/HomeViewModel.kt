package co.storibank.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.storibank.domain.model.BankMovement
import co.storibank.domain.usecase.GetBankInfoUseCase
import co.storibank.presentation.viewmodel.state.BankInfoState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
    @Inject
    constructor(
        private val getBankInfoUseCase: GetBankInfoUseCase,
    ) : ViewModel() {
        private val _bankInfoState = MutableStateFlow<BankInfoState>(BankInfoState.Idle)
        val bankInfoState: StateFlow<BankInfoState> = _bankInfoState

        fun getBankInfo() {
            viewModelScope.launch {
                _bankInfoState.value = BankInfoState.Loading

                try {
                    val bankInfo = getBankInfoUseCase()
                    _bankInfoState.value =
                        if (bankInfo != null) {
                            BankInfoState.Success(bankInfo)
                        } else {
                            BankInfoState.Error("Failed to fetch bank info")
                        }
                } catch (e: Exception) {
                    _bankInfoState.value = BankInfoState.Error("Failed to fetch bank info")
                }
            }
        }

        fun getMovementById(movementId: String): BankMovement? {
            val state = _bankInfoState.value as BankInfoState.Success
            return state.bankInfo.movements.find { it.id == movementId }
        }
    }
