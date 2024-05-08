package co.storibank.domain.model

data class BankInfo(
    val balance: Double,
    val movements: List<BankMovement>,
)
