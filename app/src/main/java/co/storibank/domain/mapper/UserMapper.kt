package co.storibank.domain.mapper

import co.storibank.data.model.UserDataModel
import co.storibank.domain.model.User
import com.google.firebase.auth.FirebaseUser

object UserMapper {
    fun UserDataModel.mapToDomain(): User {
        return User(
            email = this.email,
            password = this.password,
        )
    }

    fun User.mapToData(): UserDataModel {
        return UserDataModel(
            name = this.name,
            email = this.email,
            password = this.password,
            image = this.image ?: "",
            uid = this.uid ?: "",
        )
    }

    fun FirebaseUser.mapToDomain(): User {
        return User(
            email = this.email ?: "",
            uid = this.uid,
        )
    }
}
