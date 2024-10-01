package st.dominic.qrcodescanner.core.data.repository

import st.dominic.qrcodescanner.core.model.AuthCurrentUser

interface EmailPasswordAuthenticationRepository {
    suspend fun createUserWithEmailAndPassword(email: String, password: String): Result<Boolean>

    suspend fun signInWithEmailAndPassword(email: String, password: String): Result<Boolean>

    fun getCurrentUser(): AuthCurrentUser?
}