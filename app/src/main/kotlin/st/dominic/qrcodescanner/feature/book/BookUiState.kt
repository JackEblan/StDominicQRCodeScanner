package st.dominic.qrcodescanner.feature.book

import st.dominic.qrcodescanner.core.model.Book

sealed interface BookUiState {
    data class Success(val books: List<Book>) : BookUiState

    data object Loading : BookUiState

    data object NotSignedIn: BookUiState

    data object NotEmailVerified: BookUiState
}