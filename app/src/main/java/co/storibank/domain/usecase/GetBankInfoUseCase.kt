package co.storibank.domain.usecase

import co.storibank.domain.model.BankInfo
import co.storibank.domain.repository.BankRepository
import javax.inject.Inject

class GetBankInfoUseCase
    @Inject
    constructor(
        private val bankRepository: BankRepository,
    ) {
        suspend operator fun invoke(): BankInfo? {
            return bankRepository.getBankInfo()
        }
    }
