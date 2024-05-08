package co.storibank.data.model

import java.util.Date

data class BankMovementDataModel(
    val id: String,
    val amount: Double,
    val description: String,
    val date: Date
)