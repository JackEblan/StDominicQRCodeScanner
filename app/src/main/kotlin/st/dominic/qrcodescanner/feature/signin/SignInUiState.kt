package st.dominic.qrcodescanner.feature.signin

sealed interface SignInUiState {
    data object SignIn : SignInUiState

    data object Loading : SignInUiState
}