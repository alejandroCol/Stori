package co.storibank.domain.model

data class User(
    val email: String,
    val password: String? = "",
    val uid: String? = "",
)
