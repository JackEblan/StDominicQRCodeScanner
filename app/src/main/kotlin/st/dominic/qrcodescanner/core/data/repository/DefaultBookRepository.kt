package st.dominic.qrcodescanner.core.data.repository

import android.net.Uri
import kotlinx.coroutines.flow.Flow
import st.dominic.qrcodescanner.core.data.repository.BookRepository.Companion.BOOK_REFERENCE
import st.dominic.qrcodescanner.core.model.Book
import st.dominic.qrcodescanner.core.model.UploadFileResult
import st.dominic.qrcodescanner.core.network.firestore.BookDataSource
import st.dominic.qrcodescanner.core.network.storage.StorageDataSource
import javax.inject.Inject

class DefaultBookRepository @Inject constructor(
    private val bookDataSource: BookDataSource, private val storageDataSource: StorageDataSource
) : BookRepository {
    override fun getBorrowedBooksDocuments(studentId: String): Flow<List<Book>> {
        return bookDataSource.getBorrowedBooksDocuments(studentId = studentId)
    }

    override suspend fun addBook(book: Book) {
        bookDataSource.addBook(book = book)
    }

    override suspend fun updateBookStatus(book: Book) {
        bookDataSource.updateBookStatus(book = book)
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
}