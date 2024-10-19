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
        val authCurrentUser =
            emailPasswordAuthenticationRepository.getCurrentUser() ?: return GetProfileResult.Failed

        return if (authCurrentUser.isEmailVerified) {
            GetProfileResult.Success(
                authCurrentUser = authCurrentUser,
                isAdmin = adminRepository.isAdmin(id = authCurrentUser.uid)
            )
        } else {
            GetProfileResult.EmailVerify
        }
    }
}