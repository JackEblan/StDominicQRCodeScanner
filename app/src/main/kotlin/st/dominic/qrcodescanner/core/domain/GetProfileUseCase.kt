package st.dominic.qrcodescanner.core.domain

import st.dominic.qrcodescanner.core.data.repository.AdminRepository
import st.dominic.qrcodescanner.core.data.repository.EmailPasswordAuthenticationRepository
import st.dominic.qrcodescanner.core.model.GetProfileResult
import javax.inject.Inject

class GetProfileUseCase @Inject constructor(
    private val emailPasswordAuthenticationRepository: EmailPasswordAuthenticationRepository,
    private val adminRepository: AdminRepository,
) {
    suspend operator fun invoke(): GetProfileResult {
        val authCurrentUser = emailPasswordAuthenticationRepository.getCurrentUser()

        return if (authCurrentUser != null) {
            GetProfileResult(
                uid = authCurrentUser.uid,
                displayName = authCurrentUser.displayName,
                email = authCurrentUser.email,
                isSignedIn = true,
                isEmailVerified = authCurrentUser.isEmailVerified,
                isAdmin = adminRepository.isAdmin(id = authCurrentUser.uid)
            )
        } else {
            GetProfileResult(
                isSignedIn = false,
            )
        }
    }
}