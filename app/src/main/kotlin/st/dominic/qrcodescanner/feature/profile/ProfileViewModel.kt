package st.dominic.qrcodescanner.feature.profile

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import st.dominic.qrcodescanner.core.data.repository.EmailPasswordAuthenticationRepository
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val emailPasswordAuthenticationRepository: EmailPasswordAuthenticationRepository
) : ViewModel() {
    val authCurrentUser get() = emailPasswordAuthenticationRepository.getCurrentUser()

    fun signOut() {
        emailPasswordAuthenticationRepository.signOut()
    }
}