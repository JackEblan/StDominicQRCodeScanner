package st.dominic.qrcodescanner.feature.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import st.dominic.qrcodescanner.core.data.repository.AdminRepository
import st.dominic.qrcodescanner.core.data.repository.EmailPasswordAuthenticationRepository
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val emailPasswordAuthenticationRepository: EmailPasswordAuthenticationRepository,
    private val adminRepository: AdminRepository,
) : ViewModel() {
    val authCurrentUser get() = emailPasswordAuthenticationRepository.getCurrentUser()

    private val _isAdmin = MutableStateFlow<Boolean?>(null)

    val isAdmin = _isAdmin.onStart { checkAdmin() }.stateIn(
        scope = viewModelScope, started = SharingStarted.WhileSubscribed(5_000), initialValue = null
    )

    fun signOut() {
        emailPasswordAuthenticationRepository.signOut()
    }

    private fun checkAdmin() {
        viewModelScope.launch {
            _isAdmin.update {
                authCurrentUser?.uid != null && adminRepository.isAdmin(id = authCurrentUser?.uid!!)
            }
        }
    }
}