package st.dominic.qrcodescanner.feature.signin

sealed interface SignInUiState {
    data class Success(val isSignedIn: Boolean) : SignInUiState

    data object Loading : SignInUiState
}