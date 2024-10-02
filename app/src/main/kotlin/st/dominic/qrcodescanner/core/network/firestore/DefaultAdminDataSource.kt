package st.dominic.qrcodescanner.core.network.firestore

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import st.dominic.qrcodescanner.core.network.firestore.AdminDataSource.Companion.ADMIN_COLLECTION
import javax.inject.Inject

class DefaultAdminDataSource @Inject constructor(
    private val firestore: FirebaseFirestore,
) : AdminDataSource {

    override suspend fun isAdmin(id: String): Boolean {
        return firestore.collection(ADMIN_COLLECTION).document(id).get().await().exists()
    }
}