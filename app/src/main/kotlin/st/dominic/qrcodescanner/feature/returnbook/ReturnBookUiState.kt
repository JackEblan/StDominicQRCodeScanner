package st.dominic.qrcodescanner.feature.returnbook

import st.dominic.qrcodescanner.core.model.Book

sealed interface ReturnBookUiState {
    data class Success(val book: Book?) : ReturnBookUiState

    data object Loading : ReturnBookUiState

    data object Returned : ReturnBookUiState
}