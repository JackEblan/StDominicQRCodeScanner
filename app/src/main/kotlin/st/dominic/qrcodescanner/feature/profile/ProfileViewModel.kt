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
import st.dominic.qrcodescanner.core.data.repository.EmailPasswordAuthenticationRepository
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val emailPasswordAuthenticationRepository: EmailPasswordAuthenticationRepository
) : ViewModel() {
    private val _profileUiState = MutableStateFlow<ProfileUiState?>(null)

    val profileUiState = _profileUiState.onStart { getCurrentUser() }.stateIn(
        scope = viewModelScope, started = SharingStarted.WhileSubscribed(5_000), initialValue = null
    )

    fun signOut() {
        emailPasswordAuthenticationRepository.signOut()
    }

    private fun getCurrentUser() {
        viewModelScope.launch {
            _profileUiState.update {
                ProfileUiState.Loading
            }

            _profileUiState.update {
                ProfileUiState.Success(
                    authCurrentUser = emailPasswordAuthenticationRepository.getCurrentUser()
                        .getOrNull()
                )
            }
        }
    }
}