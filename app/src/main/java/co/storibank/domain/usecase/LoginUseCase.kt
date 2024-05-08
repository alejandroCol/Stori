package co.storibank.domain.usecase

import co.storibank.domain.repository.UserRepository
import javax.inject.Inject

class LoginUseCase
    @Inject
    constructor(private val userRepository: UserRepository) {
        suspend operator fun invoke(
            email: String,
            password: String,
        ): Boolean {
            return userRepository.loginUser(email, password)
        }
    }
