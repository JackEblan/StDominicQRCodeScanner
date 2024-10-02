package st.dominic.qrcodescanner.core.data.repository

interface AdminRepository {
    suspend fun isAdmin(id: String): Boolean
}