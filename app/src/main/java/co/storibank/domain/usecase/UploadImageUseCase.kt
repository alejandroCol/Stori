package co.storibank.domain.usecase

import co.storibank.domain.repository.UserRepository
import javax.inject.Inject

class UploadImageUseCase
    @Inject
    constructor(private val userRepository: UserRepository) {
        suspend operator fun invoke(
            userId: String,
            imageBytes: ByteArray,
        ): Boolean {
            val imageName = "profile_$userId.png"
            return userRepository.uploadUserImage(userId, imageName, imageBytes)
        }
    }
