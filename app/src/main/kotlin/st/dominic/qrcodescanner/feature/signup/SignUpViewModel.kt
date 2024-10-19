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
    private val _signUpUiState = MutableStateFlow<SignUpUiState>(SignUpUiState.SignUp)

    val signUpUiState = _signUpUiState.asStateFlow()

    private val _snackbar = MutableStateFlow<String?>(null)

    val snackbar = _snackbar.asStateFlow()

    private val _success = MutableStateFlow<Boolean?>(null)

    val navigateUp = _success.asStateFlow()

    fun createUserWithEmailAndPassword(name: String, email: String, password: String) {
        viewModelScope.launch {
            _signUpUiState.update {
                SignUpUiState.Loading
            }

            emailPasswordAuthenticationRepository.createUserWithEmailAndPassword(
                name = name, email = email, password = password
            ).onSuccess { success ->
                _success.update {
                    success
                }

            }.onFailure { t ->
                _signUpUiState.update {
                    SignUpUiState.SignUp
                }

                _snackbar.update {
                    t.message
                }
            }
        }
    }
}