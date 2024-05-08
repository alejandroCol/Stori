package co.storibank.data.repository

import co.storibank.data.datasource.remote.FirebaseAuthDataSource
import co.storibank.data.datasource.remote.FirebaseBankDataSource
import co.storibank.domain.mapper.BankInfoMapper.toDomain
import co.storibank.domain.model.BankInfo
import co.storibank.domain.repository.BankRepository
import javax.inject.Inject

class BankRepositoryImpl
    @Inject
    constructor(
        private val bankDataSource: FirebaseBankDataSource,
        private val firebaseAuthDataSource: FirebaseAuthDataSource,
    ) : BankRepository {
        override suspend fun getBankInfo(): BankInfo? {
            val userId = firebaseAuthDataSource.getCurrentUser()?.uid ?: ""
            val bankInfoDataModel = bankDataSource.getBankInfo(userId)
            return bankInfoDataModel?.toDomain()
        }
    }
