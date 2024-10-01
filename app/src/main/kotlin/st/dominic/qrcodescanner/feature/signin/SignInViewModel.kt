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
    private val _signInResult = MutableStateFlow<Boolean?>(null)

    val signInResult = _signInResult.asStateFlow()

    private val _signInErrorMessage = MutableStateFlow<String?>(null)

    val signInErrorMessage = _signInErrorMessage.asStateFlow()

    fun signInWithEmailAndPassword(email: String, password: String) {
        viewModelScope.launch {
            emailPasswordAuthenticationRepository.signInWithEmailAndPassword(
                email = email, password = password
            ).onSuccess { result ->
                _signInResult.update {
                    result
                }
            }.onFailure { t ->
                _signInErrorMessage.update {
                    t.message
                }
            }
        }
    }
}