package st.dominic.qrcodescanner.core.data.repository

import kotlinx.coroutines.flow.Flow
import st.dominic.qrcodescanner.core.model.Book

interface BookRepository {
    fun getBorrowedBooksDocuments(studentId: String): Flow<List<Book>>

    suspend fun addBook(book: Book)

    suspend fun updateBookStatus(book: Book)

    suspend fun deleteBook(id: String)
}