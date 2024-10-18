package st.dominic.qrcodescanner.core.network.auth

import st.dominic.qrcodescanner.core.model.AuthCurrentUser

interface EmailPasswordAuthentication {
    suspend fun createUserWithEmailAndPassword(name: String, email: String, password: String): Result<Boolean>

    suspend fun signInWithEmailAndPassword(email: String, password: String): Result<Boolean>

    suspend fun sendEmailVerification(): Result<Boolean>

    fun getCurrentUser(): AuthCurrentUser?

    fun signOut()
}