package st.dominic.qrcodescanner.core.data.repository

import android.net.Uri
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import st.dominic.qrcodescanner.core.model.Book
import st.dominic.qrcodescanner.core.model.BookStatus

interface BookRepository {
    val progress: SharedFlow<Float?>

    suspend fun getBorrowedBooksByStudentId(studentId: String): List<Book>

    fun getBorrowedBooks(): Flow<List<Book>>

    suspend fun addBook(book: Book)

    suspend fun updateBookStatus(id: String, bookStatus: BookStatus)

    suspend fun deleteBook(id: String)

    suspend fun uploadBookPhoto(
        file: Uri, fileName: String
    ): Result<String>

    fun generateBookId(): String

    suspend fun getBook(id: String): Book?

    companion object {
        const val BOOK_REFERENCE = "book"
    }
}