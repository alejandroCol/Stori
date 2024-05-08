package co.storibank.domain.model

import java.util.Date

data class BankMovement(
    val id: String,
    val amount: Double,
    val description: String,
    val date: Date,
)
