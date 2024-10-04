package st.dominic.qrcodescanner.core.data.repository

import android.net.Uri
import kotlinx.coroutines.flow.Flow
import st.dominic.qrcodescanner.core.model.Book
import st.dominic.qrcodescanner.core.model.BookStatus
import st.dominic.qrcodescanner.core.model.UploadFileResult

interface BookRepository {
    suspend fun getBorrowedBooksByStudentId(studentId: String): List<Book>

    fun getBorrowedBooks(): Flow<List<Book>>

    suspend fun addBook(book: Book)

    suspend fun updateBookStatus(id: String, bookStatus: BookStatus)

    suspend fun deleteBook(id: String)

    fun uploadBookPhoto(
        file: Uri, fileName: String
    ): Flow<UploadFileResult>


    fun generateBookId(): String

    suspend fun getBook(id: String): Book?

    companion object {
        const val BOOK_REFERENCE = "book"
    }
}