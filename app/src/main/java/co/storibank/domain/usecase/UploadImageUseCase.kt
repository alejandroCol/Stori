package co.storibank.domain.usecase

import co.storibank.domain.model.User
import co.storibank.domain.repository.UserRepository
import javax.inject.Inject

class UploadImageUseCase
    @Inject
    constructor(private val userRepository: UserRepository) {
        suspend operator fun invoke(
            user: User,
            imageBytes: ByteArray,
        ): Boolean {
            user.image = "profile_${user.uid}.png"
            return userRepository.uploadUserImage(user, imageBytes)
        }
    }
