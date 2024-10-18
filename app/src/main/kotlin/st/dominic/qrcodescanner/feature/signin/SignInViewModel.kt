package st.dominic.qrcodescanner.feature.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import st.dominic.qrcodescanner.core.data.repository.EmailPasswordAuthenticationRepository
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(private val emailPasswordAuthenticationRepository: EmailPasswordAuthenticationRepository) :
    ViewModel() {
    private val _signInUiState = MutableStateFlow<SignInUiState?>(null)

    val signInUiState = _signInUiState.asStateFlow()

    private val _snackbar = MutableStateFlow<String?>(null)

    val snackbar = _snackbar.asStateFlow()

    private val _emailVerificationResult = MutableStateFlow<Boolean?>(null)

    val emailVerificationResult = _emailVerificationResult.asStateFlow()

    private val _sendPasswordResetEmailResult = MutableStateFlow<Boolean?>(null)

    val sendPasswordResetEmailResult = _sendPasswordResetEmailResult.asStateFlow()

    fun verifyEmail() {
        viewModelScope.launch {
            emailPasswordAuthenticationRepository.sendEmailVerification().onSuccess { success ->
                _emailVerificationResult.update {
                    success
                }
            }.onFailure { t ->
                _snackbar.update {
                    t.localizedMessage
                }
            }
        }
    }

    fun signInWithEmailAndPassword(email: String, password: String) {
        viewModelScope.launch {
            _signInUiState.update {
                SignInUiState.Loading
            }

            emailPasswordAuthenticationRepository.signInWithEmailAndPassword(
                email = email, password = password
            ).onSuccess { result ->
                _signInUiState.update {
                    SignInUiState.Success(isSignedIn = result)
                }
            }.onFailure { t ->
                _signInUiState.update {
                    null
                }

                _snackbar.update {
                    t.message
                }
            }
        }
    }

    fun sendPasswordResetEmail(email: String) {
        viewModelScope.launch {
            emailPasswordAuthenticationRepository.sendPasswordResetEmail(
                email = email,
            ).onSuccess { success ->
                _sendPasswordResetEmailResult.update {
                    success
                }
            }.onFailure { t ->
                _snackbar.update {
                    t.message
                }
            }
        }
    }
}