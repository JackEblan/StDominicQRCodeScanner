package st.dominic.qrcodescanner.core.network.auth

import st.dominic.qrcodescanner.core.model.AuthCurrentUser

interface EmailPasswordAuthentication {
    suspend fun createUserWithEmailAndPassword(email: String, password: String): Result<Boolean>

    suspend fun signInWithEmailAndPassword(email: String, password: String): Result<Boolean>

    fun getCurrentUser(): AuthCurrentUser?
}