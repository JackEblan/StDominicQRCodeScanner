package st.dominic.qrcodescanner.feature.signup

sealed interface SignUpUiState {
    data object SignUp : SignUpUiState

    data object Loading : SignUpUiState
}