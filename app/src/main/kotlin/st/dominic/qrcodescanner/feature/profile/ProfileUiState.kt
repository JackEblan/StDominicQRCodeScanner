package st.dominic.qrcodescanner.feature.profile

import st.dominic.qrcodescanner.core.model.AuthCurrentUser

sealed interface ProfileUiState {
    data class Success(val authCurrentUser: AuthCurrentUser, val isAdmin: Boolean) : ProfileUiState

    data object Loading : ProfileUiState

    data object Failed : ProfileUiState

    data object EmailVerify : ProfileUiState
}