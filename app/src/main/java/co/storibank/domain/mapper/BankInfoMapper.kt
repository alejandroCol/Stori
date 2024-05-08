package co.storibank.domain.mapper

import co.storibank.data.model.BankInfoDataModel
import co.storibank.data.model.BankMovementDataModel
import co.storibank.domain.model.BankInfo
import co.storibank.domain.model.BankMovement

object BankInfoMapper {
    fun BankInfoDataModel.toDomain(): BankInfo {
        return BankInfo(
            balance = this.balance,
            movements = this.movements.map { it.toDomain() },
        )
    }

    fun BankInfo.toDataModel(): BankInfoDataModel {
        return BankInfoDataModel(
            balance = this.balance,
            movements = this.movements.map { it.toDataModel() },
        )
    }

    private fun BankMovementDataModel.toDomain(): BankMovement {
        return BankMovement(
            id = this.id,
            amount = this.amount,
            description = this.description,
            date = this.date,
        )
    }

    private fun BankMovement.toDataModel(): BankMovementDataModel {
        return BankMovementDataModel(
            id = this.id,
            amount = this.amount,
            description = this.description,
            date = this.date,
        )
    }
}
