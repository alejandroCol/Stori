package co.storibank.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.storibank.R
import co.storibank.domain.model.BankMovement
import co.storibank.domain.usecase.GetBankInfoUseCase
import co.storibank.presentation.viewmodel.state.BankInfoState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
    @Inject
    constructor(
        private val getBankInfoUseCase: GetBankInfoUseCase,
        @ApplicationContext private val context: Context,
    ) : ViewModel() {
        private val _bankInfoState = MutableStateFlow<BankInfoState>(BankInfoState.Idle)
        val bankInfoState: StateFlow<BankInfoState> = _bankInfoState

        fun fetchBankInfo() {
            viewModelScope.launch {
                _bankInfoState.value = BankInfoState.Loading
                try {
                    val bankInfo = getBankInfoUseCase() ?: throw Exception(context.getString(R.string.bank_info_null))
                    _bankInfoState.value = BankInfoState.Success(bankInfo)
                } catch (e: Exception) {
                    val errorMessage = context.getString(R.string.error_fetching_bank_info, e.message)
                    _bankInfoState.value = BankInfoState.Error(errorMessage)
                }
            }
        }

        fun findMovementById(movementId: String): BankMovement? {
            return (_bankInfoState.value as? BankInfoState.Success)?.bankInfo?.movements?.find { it.id == movementId }
        }
    }
