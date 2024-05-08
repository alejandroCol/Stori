package co.storibank.data.datasource.remote

import co.storibank.domain.model.User
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
            userId: String,
            imageName: String,
            imageBytes: ByteArray,
        ): Boolean {
            val reference = firebaseStorage.reference.child(imageName)

            val uploadTask = reference.putStream(ByteArrayInputStream(imageBytes))

            firebaseFirestore.collection("users").document(userId).set(User(userId)).await()

            return try {
                uploadTask.await()
                val imageUrl = reference.downloadUrl.await().toString()
                firebaseFirestore.collection("users").document(userId).update("image", imageUrl).await()
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }
