package co.storibank.data.datasource.remote

import co.storibank.data.model.UserDataModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseAuthDataSource
    @Inject
    constructor(private val firebaseAuth: FirebaseAuth) {
        suspend fun registerUser(userDataModel: UserDataModel): Boolean {
            return try {
                userDataModel.password?.let {
                    firebaseAuth.createUserWithEmailAndPassword(
                        userDataModel.email,
                        it,
                    ).await()
                }
                true
            } catch (e: Exception) {
                false
            }
        }

        suspend fun loginUser(
            email: String,
            password: String,
        ): Boolean {
            return try {
                firebaseAuth.signInWithEmailAndPassword(
                    email,
                    password,
                ).await()
                true
            } catch (e: Exception) {
                false
            }
        }

        fun getCurrentUser() = firebaseAuth.currentUser
    }
