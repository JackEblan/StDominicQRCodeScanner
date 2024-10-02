package st.dominic.qrcodescanner.feature.admin

import st.dominic.qrcodescanner.core.model.Book

sealed interface AdminUiState {
    data class Success(val book: Book?) : AdminUiState

    data object Loading : AdminUiState
}