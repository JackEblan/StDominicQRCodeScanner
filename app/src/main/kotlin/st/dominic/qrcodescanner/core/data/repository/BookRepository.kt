package st.dominic.qrcodescanner.core.data.repository

import android.net.Uri
import kotlinx.coroutines.flow.Flow
import st.dominic.qrcodescanner.core.model.Book
import st.dominic.qrcodescanner.core.model.UploadFileResult

interface BookRepository {
    fun getBorrowedBooksDocuments(studentId: String): Flow<List<Book>>

    suspend fun addBook(book: Book)

    suspend fun updateBookStatus(book: Book)

    suspend fun deleteBook(id: String)

    fun uploadBookPhoto(
        file: Uri, fileName: String
    ): Flow<UploadFileResult>


    fun generateBookId(): String

    companion object {
        const val BOOK_REFERENCE = "book"
    }
}