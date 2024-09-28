package st.dominic.qrcodescanner.core.network.firestore

import kotlinx.coroutines.flow.Flow
import st.dominic.qrcodescanner.core.model.Book

interface BookDataSource {
    fun getBorrowedBooksDocuments(studentId: String): Flow<List<Book>>

    companion object {
        const val BOOK_COLLECTION = "book"
    }
}