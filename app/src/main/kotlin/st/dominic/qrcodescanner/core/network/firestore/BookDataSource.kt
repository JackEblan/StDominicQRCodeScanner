package st.dominic.qrcodescanner.core.network.firestore

import kotlinx.coroutines.flow.Flow
import st.dominic.qrcodescanner.core.model.Book
import st.dominic.qrcodescanner.core.model.BookStatus

interface BookDataSource {
    suspend fun getBorrowedBooksDocumentsByStudentId(studentId: String): Flow<List<Book>>

    fun getBorrowedBooksDocuments(): Flow<List<Book>>

    suspend fun addBook(book: Book)

    suspend fun updateBookStatus(id: String, bookStatus: BookStatus)

    suspend fun deleteBook(id: String)

    fun generateBookId(): String

    suspend fun getBook(id: String): Book?

    companion object {
        const val BOOK_COLLECTION = "book"
    }
}