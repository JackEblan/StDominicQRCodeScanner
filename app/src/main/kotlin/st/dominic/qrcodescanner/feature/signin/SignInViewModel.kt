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
    private val _signInUiState = MutableStateFlow<SignInUiState>(SignInUiState.SignIn)

    val signInUiState = _signInUiState.asStateFlow()

    private val _snackbar = MutableStateFlow<String?>(null)

    val snackbar = _snackbar.asStateFlow()

    private val _sendPasswordResetEmailResult = MutableStateFlow<Boolean?>(null)

    val sendPasswordResetEmailResult = _sendPasswordResetEmailResult.asStateFlow()

    private val _navigateUp = MutableStateFlow<Boolean?>(null)

    val navigateUp = _navigateUp.asStateFlow()

    fun signInWithEmailAndPassword(email: String, password: String) {
        viewModelScope.launch {
            _signInUiState.update {
                SignInUiState.Loading
            }

            emailPasswordAuthenticationRepository.signInWithEmailAndPassword(
                email = email, password = password
            ).onSuccess { success ->
                _navigateUp.update {
                    success
                }

            }.onFailure { t ->
                _snackbar.update {
                    t.message
                }
            }
        }
    }

    fun sendPasswordResetEmail(email: String) {
        viewModelScope.launch {
            _signInUiState.update {
                SignInUiState.Loading
            }

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

            _signInUiState.update {
                SignInUiState.SignIn
            }
        }
    }
}