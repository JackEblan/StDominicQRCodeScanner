package st.dominic.qrcodescanner.feature.profile

import st.dominic.qrcodescanner.core.model.AuthCurrentUser

sealed interface ProfileUiState {
    data class Success(val authCurrentUser: AuthCurrentUser?): ProfileUiState

    data object Loading: ProfileUiState

    data object NotSignedIn: ProfileUiState

    data object NotEmailVerified: ProfileUiState

    data object Admin: ProfileUiState
}