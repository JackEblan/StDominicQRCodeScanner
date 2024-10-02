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
    private val _signUpResult = MutableStateFlow<Boolean?>(null)

    val signUpResult = _signUpResult.asStateFlow()

    private val _signUpErrorMessage = MutableStateFlow<String?>(null)

    val signUpErrorMessage = _signUpErrorMessage.asStateFlow()

    fun createUserWithEmailAndPassword(name: String, email: String, password: String) {
        viewModelScope.launch {
            emailPasswordAuthenticationRepository.createUserWithEmailAndPassword(
                name = name, email = email, password = password
            ).onSuccess { result ->
                _signUpResult.update {
                    result
                }
            }.onFailure { t ->
                _signUpErrorMessage.update {
                    t.message
                }
            }
        }
    }
}