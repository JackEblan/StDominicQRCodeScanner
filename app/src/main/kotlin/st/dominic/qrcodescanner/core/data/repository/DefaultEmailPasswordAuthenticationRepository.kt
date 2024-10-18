package st.dominic.qrcodescanner.core.data.repository

import st.dominic.qrcodescanner.core.model.AuthCurrentUser
import st.dominic.qrcodescanner.core.network.auth.EmailPasswordAuthentication
import javax.inject.Inject

class DefaultEmailPasswordAuthenticationRepository @Inject constructor(private val emailPasswordAuthentication: EmailPasswordAuthentication) :
    EmailPasswordAuthenticationRepository {
    override suspend fun createUserWithEmailAndPassword(
        name: String, email: String, password: String
    ): Result<Boolean> {
        return emailPasswordAuthentication.createUserWithEmailAndPassword(
            name = name, email = email, password = password
        )
    }

    override suspend fun signInWithEmailAndPassword(
        email: String, password: String
    ): Result<Boolean> {
        return emailPasswordAuthentication.signInWithEmailAndPassword(
            email = email, password = password
        )
    }

    override suspend fun sendEmailVerification(): Result<Boolean> {
        return emailPasswordAuthentication.sendEmailVerification()
    }

    override fun getCurrentUser(): AuthCurrentUser? {
        return emailPasswordAuthentication.getCurrentUser()
    }

    override fun signOut() {
        emailPasswordAuthentication.signOut()
    }
}