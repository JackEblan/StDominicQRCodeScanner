package st.dominic.qrcodescanner.core.model

sealed interface GetBooksResult {
    data class Success(val studentId: String) : GetBooksResult

    data object Failed : GetBooksResult

    data object EmailVerify : GetBooksResult
}