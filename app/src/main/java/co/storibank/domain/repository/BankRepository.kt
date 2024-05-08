package co.storibank.domain.repository

import co.storibank.domain.model.BankInfo

interface BankRepository {
    suspend fun getBankInfo(): BankInfo?
}
