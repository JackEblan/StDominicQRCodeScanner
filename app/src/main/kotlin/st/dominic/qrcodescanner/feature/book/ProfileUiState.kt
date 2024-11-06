package st.dominic.qrcodescanner.feature.book

sealed interface ProfileUiState {
    data object Success : ProfileUiState

    data object Loading : ProfileUiState

    data object Failed : ProfileUiState

    data object EmailVerify : ProfileUiState
}