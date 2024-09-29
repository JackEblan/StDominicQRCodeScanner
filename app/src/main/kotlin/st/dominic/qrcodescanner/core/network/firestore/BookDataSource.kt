package st.dominic.qrcodescanner.core.network.firestore

import kotlinx.coroutines.flow.Flow
import st.dominic.qrcodescanner.core.model.Book

interface BookDataSource {
    fun getBorrowedBooksDocuments(studentId: String): Flow<List<Book>>

    suspend fun addBook(book: Book)

    suspend fun updateBookStatus(book: Book)

    suspend fun deleteBook(id: String)

    fun generateBookId(): String

    companion object {
        const val BOOK_COLLECTION = "book"
    }
}