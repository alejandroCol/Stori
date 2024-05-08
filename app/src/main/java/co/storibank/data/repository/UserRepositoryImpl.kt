package co.storibank.data.repository

import co.storibank.data.datasource.remote.FirebaseAuthDataSource
import co.storibank.data.datasource.remote.FirebaseStorageDataSource
import co.storibank.domain.mapper.UserMapper.mapToData
import co.storibank.domain.mapper.UserMapper.mapToDomain
import co.storibank.domain.model.User
import co.storibank.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl
    @Inject
    constructor(
        private val firebaseAuthDataSource: FirebaseAuthDataSource,
        private val firebaseStorage: FirebaseStorageDataSource,
    ) :
    UserRepository {
        override suspend fun registerUser(user: User): Boolean {
            return firebaseAuthDataSource.registerUser(user.mapToData())
        }

        override suspend fun loginUser(
            email: String,
            password: String,
        ): Boolean {
            return firebaseAuthDataSource.loginUser(email, password)
        }

        override fun getCurrentUser(): User? {
            val currentUser = firebaseAuthDataSource.getCurrentUser()
            return currentUser?.let { it.mapToDomain() }
        }

        override suspend fun uploadUserImage(
            userId: String,
            imageName: String,
            imageBytes: ByteArray,
        ): Boolean {
            return firebaseStorage.uploadImage(userId, imageName, imageBytes)
        }
    }
