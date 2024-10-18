package st.dominic.qrcodescanner.core.data.repository

import st.dominic.qrcodescanner.core.model.AuthCurrentUser

interface EmailPasswordAuthenticationRepository {
    suspend fun createUserWithEmailAndPassword(name: String, email: String, password: String): Result<Boolean>

    suspend fun signInWithEmailAndPassword(email: String, password: String): Result<Boolean>

    suspend fun sendEmailVerification(): Result<Boolean>

    fun getCurrentUser(): AuthCurrentUser?

    fun signOut()
}