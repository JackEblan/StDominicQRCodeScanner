package st.dominic.qrcodescanner.core.model

sealed interface GetBooksResult {
    data class Success(val books: List<Book>): GetBooksResult

    data object NotSignedIn: GetBooksResult

    data object NotEmailVerified: GetBooksResult
}