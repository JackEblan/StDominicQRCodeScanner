package st.dominic.qrcodescanner.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import st.dominic.qrcodescanner.core.domain.GetProfileUseCase
import st.dominic.qrcodescanner.core.model.GetProfileResult
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getProfileUseCase: GetProfileUseCase,
) : ViewModel() {
    private val _profile = MutableStateFlow<GetProfileResult?>(null)

    val profile = _profile.onStart { getProfile() }.stateIn(
        scope = viewModelScope, started = SharingStarted.WhileSubscribed(), initialValue = null
    )

    private fun getProfile() {
        viewModelScope.launch {
            _profile.update {
                getProfileUseCase()
            }
        }
    }
}