package st.dominic.qrcodescanner.feature.signup

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
class SignUpViewModel @Inject constructor(private val emailPasswordAuthenticationRepository: EmailPasswordAuthenticationRepository) :
    ViewModel() {
    private val _signUpUiState = MutableStateFlow<SignUpUiState?>(null)

    val signUpUiState = _signUpUiState.asStateFlow()

    private val _signUpErrorMessage = MutableStateFlow<String?>(null)

    val signUpErrorMessage = _signUpErrorMessage.asStateFlow()

    fun createUserWithEmailAndPassword(name: String, email: String, password: String) {
        viewModelScope.launch {
            _signUpUiState.update {
                SignUpUiState.Loading
            }
            emailPasswordAuthenticationRepository.createUserWithEmailAndPassword(
                name = name, email = email, password = password
            ).onSuccess { result ->
                _signUpUiState.update {
                    SignUpUiState.Success(isSignedUp = result)
                }
            }.onFailure { t ->
                _signUpUiState.update {
                    null
                }

                _signUpErrorMessage.update {
                    t.message
                }
            }
        }
    }
}