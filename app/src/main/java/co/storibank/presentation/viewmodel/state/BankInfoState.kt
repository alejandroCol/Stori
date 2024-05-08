package co.storibank.presentation.viewmodel.state

import co.storibank.domain.model.BankInfo

sealed class BankInfoState {
    data object Idle : BankInfoState()

    data object Loading : BankInfoState()

    data class Success(val bankInfo: BankInfo) : BankInfoState()

    data class Error(val message: String) : BankInfoState()
}
