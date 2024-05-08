package co.storibank.domain.repository

import co.storibank.domain.model.User

interface UserRepository {
    suspend fun registerUser(user: User): Boolean

    suspend fun loginUser(
        email: String,
        password: String,
    ): Boolean

    fun getCurrentUser(): User?

    suspend fun uploadUserImage(
        userId: String,
        imageName: String,
        imageBytes: ByteArray,
    ): Boolean
}
