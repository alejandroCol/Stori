package co.storibank.data.datasource.remote

import android.content.ContentValues.TAG
import android.util.Log
import co.storibank.data.model.BankInfoDataModel
import co.storibank.data.model.BankMovementDataModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.Date
import javax.inject.Inject

class FirebaseBankDataSource
    @Inject
    constructor(
        private val firestore: FirebaseFirestore,
    ) {
        suspend fun getBankInfo(userId: String): BankInfoDataModel? {
            var bankInfo: BankInfoDataModel? = null
            try {
                val userDocument = firestore.collection("users").document(userId).get().await()
                if (userDocument.exists()) {
                    val balance = userDocument.getDouble("balance") ?: 0.0
                    val movements = mutableListOf<BankMovementDataModel>()
                    val bankInfoSnapshot =
                        FirebaseFirestore.getInstance()
                            .collection("bank_movements")
                            .document(userId)
                            .collection("movements")

                    val querySnapshot = bankInfoSnapshot.get().await()

                    for (document in querySnapshot.documents) {
                        val id = document.id
                        val amount = document.getDouble("amount") ?: 0.0
                        val description = document.getString("description") ?: ""
                        val date = document.getTimestamp("date")?.toDate() ?: Date()

                        val movement = BankMovementDataModel(id, amount, description, date)
                        movements.add(movement)
                    }
                    bankInfo = BankInfoDataModel(balance, movements)
                } else {
                    Log.e(TAG, "User document does not exist")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error getting bank info: $e")
            }

            return bankInfo
        }
    }
