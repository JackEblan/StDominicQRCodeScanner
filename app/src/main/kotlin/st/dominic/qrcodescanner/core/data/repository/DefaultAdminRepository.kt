package st.dominic.qrcodescanner.core.data.repository

import st.dominic.qrcodescanner.core.network.firestore.AdminDataSource
import javax.inject.Inject

class DefaultAdminRepository @Inject constructor(
    private val adminDataSource: AdminDataSource,
) : AdminRepository {
    override suspend fun isAdmin(id: String): Boolean {
        return adminDataSource.isAdmin(id = id)
    }
}