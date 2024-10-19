package st.dominic.qrcodescanner.core.model

sealed interface GetBooksResult {
    data class Success(val books: List<Book>): GetBooksResult

    data object Failed: GetBooksResult

    data object EmailVerify: GetBooksResult
}