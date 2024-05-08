package co.storibank.data.model

data class BankInfoDataModel(
    val balance: Double,
    val movements: List<BankMovementDataModel>
)
