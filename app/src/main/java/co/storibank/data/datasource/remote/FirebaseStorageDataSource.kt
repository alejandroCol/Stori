package co.storibank.data.datasource.remote

import co.storibank.data.model.UserDataModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayInputStream
import javax.inject.Inject

class FirebaseStorageDataSource
    @Inject
    constructor(
        private val firebaseStorage: FirebaseStorage,
        private val firebaseFirestore: FirebaseFirestore,
    ) {
        suspend fun uploadImage(
            user: UserDataModel,
            imageBytes: ByteArray,
        ): Boolean {
            val reference = firebaseStorage.reference.child(user.image)

            val uploadTask = reference.putStream(ByteArrayInputStream(imageBytes))

            firebaseFirestore.collection("users")
                .document(user.uid).set(user).await()

            return try {
                uploadTask.await()
                val imageUrl = reference.downloadUrl.await().toString()
                firebaseFirestore.collection("users")
                    .document(user.uid).update("image", imageUrl).await()
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }
