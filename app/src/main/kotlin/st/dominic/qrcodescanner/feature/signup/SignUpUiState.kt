package st.dominic.qrcodescanner.feature.signup

sealed interface SignUpUiState {
    data class Success(val isSignedUp: Boolean) : SignUpUiState

    data object Loading : SignUpUiState
}