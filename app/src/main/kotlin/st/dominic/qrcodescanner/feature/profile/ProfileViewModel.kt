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
import st.dominic.qrcodescanner.core.domain.GetProfileUseCase
import st.dominic.qrcodescanner.core.model.GetProfileResult
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val emailPasswordAuthenticationRepository: EmailPasswordAuthenticationRepository,
    private val getProfileUseCase: GetProfileUseCase,
) : ViewModel() {
    private val _profileUiState = MutableStateFlow<ProfileUiState?>(null)

    val profileUiState = _profileUiState.onStart { getProfile() }.stateIn(
        scope = viewModelScope, started = SharingStarted.WhileSubscribed(), initialValue = null
    )

    fun signOut() {
        emailPasswordAuthenticationRepository.signOut()
    }

    private fun getProfile() {
        viewModelScope.launch {
            _profileUiState.update {
                ProfileUiState.Loading
            }

            when (val getProfileResult = getProfileUseCase()) {
                GetProfileResult.EmailVerify -> {
                    _profileUiState.update {
                        ProfileUiState.EmailVerify
                    }
                }

                GetProfileResult.Failed -> {
                    _profileUiState.update {
                        ProfileUiState.Failed
                    }
                }

                is GetProfileResult.Success -> {
                    _profileUiState.update {
                        ProfileUiState.Success(
                            authCurrentUser = getProfileResult.authCurrentUser,
                            isAdmin = getProfileResult.isAdmin
                        )
                    }
                }
            }
        }
    }
}