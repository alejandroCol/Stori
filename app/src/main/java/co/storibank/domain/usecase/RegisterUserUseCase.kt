package co.storibank.domain.usecase

import co.storibank.domain.model.User
import co.storibank.domain.repository.UserRepository
import javax.inject.Inject

class RegisterUserUseCase
    @Inject
    constructor(private val userRepository: UserRepository) {
        suspend operator fun invoke(
            name: String,
            email: String,
            password: String,
        ): String {
            val userDataModel = User(name, email, password)
            val isRegistered = userRepository.registerUser(userDataModel)
            if (isRegistered) {
                val currentUser = userRepository.getCurrentUser()
                return currentUser?.uid ?: throw Exception("User registration failed")
            } else {
                throw Exception("User registration failed")
            }
        }
    }
