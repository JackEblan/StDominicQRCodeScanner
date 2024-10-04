package st.dominic.qrcodescanner.core.data.repository

import android.net.Uri
import kotlinx.coroutines.flow.Flow
import st.dominic.qrcodescanner.core.data.repository.BookRepository.Companion.BOOK_REFERENCE
import st.dominic.qrcodescanner.core.model.Book
import st.dominic.qrcodescanner.core.model.BookStatus
import st.dominic.qrcodescanner.core.model.UploadFileResult
import st.dominic.qrcodescanner.core.network.firestore.BookDataSource
import st.dominic.qrcodescanner.core.network.storage.StorageDataSource
import javax.inject.Inject

class DefaultBookRepository @Inject constructor(
    private val bookDataSource: BookDataSource, private val storageDataSource: StorageDataSource
) : BookRepository {
    override suspend fun getBorrowedBooksByStudentId(studentId: String): List<Book> {
        return bookDataSource.getBorrowedBooksDocumentsByStudentId(studentId = studentId)
    }

    override fun getBorrowedBooks(): Flow<List<Book>> {
        return bookDataSource.getBorrowedBooksDocuments()
    }

    override suspend fun addBook(book: Book) {
        bookDataSource.addBook(book = book)
    }

    override suspend fun updateBookStatus(id: String, bookStatus: BookStatus) {
        bookDataSource.updateBookStatus(id = id, bookStatus = bookStatus)
    }

    override suspend fun deleteBook(id: String) {
        bookDataSource.deleteBook(id = id)
    }

    override fun uploadBookPhoto(file: Uri, fileName: String): Flow<UploadFileResult> {
        return storageDataSource.uploadFile(file = file, ref = BOOK_REFERENCE, fileName = fileName)
    }

    override fun generateBookId(): String {
        return bookDataSource.generateBookId()
    }

    override suspend fun getBook(id: String): Book? {
        return bookDataSource.getBook(id = id)
    }
}