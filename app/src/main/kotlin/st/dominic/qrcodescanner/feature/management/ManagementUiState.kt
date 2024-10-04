package st.dominic.qrcodescanner.feature.management

import st.dominic.qrcodescanner.core.model.Book

sealed interface ManagementUiState {
    data class Success(val books: List<Book>) : ManagementUiState

    data object Loading : ManagementUiState

    data object Failed : ManagementUiState
}