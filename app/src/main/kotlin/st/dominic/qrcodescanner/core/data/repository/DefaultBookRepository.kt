package st.dominic.qrcodescanner.core.data.repository

import kotlinx.coroutines.flow.Flow
import st.dominic.qrcodescanner.core.model.Book
import st.dominic.qrcodescanner.core.network.firestore.BookDataSource
import javax.inject.Inject

class DefaultBookRepository @Inject constructor(private val bookDataSource: BookDataSource) :
    BookRepository {
    override fun getBorrowedBooksDocuments(studentId: String): Flow<List<Book>> {
        return bookDataSource.getBorrowedBooksDocuments(studentId = studentId)
    }
}