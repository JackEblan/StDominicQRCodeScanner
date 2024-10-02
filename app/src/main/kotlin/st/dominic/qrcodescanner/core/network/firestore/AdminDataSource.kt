package st.dominic.qrcodescanner.core.network.firestore

interface AdminDataSource {
    suspend fun isAdmin(id: String): Boolean

    companion object {
        const val ADMIN_COLLECTION = "admin"
    }
}