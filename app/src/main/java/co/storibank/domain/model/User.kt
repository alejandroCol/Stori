package co.storibank.domain.model

data class User(
    val name: String? = "",
    val email: String,
    val password: String? = null,
    var image: String? = "",
    val uid: String? = "",
)
