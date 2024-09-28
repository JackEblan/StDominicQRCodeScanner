package st.dominic.qrcodescanner.core.network.firestore

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.snapshots
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.tasks.await
import st.dominic.qrcodescanner.core.model.Book
import st.dominic.qrcodescanner.core.network.firestore.BookDataSource.Companion.BOOK_COLLECTION
import st.dominic.qrcodescanner.core.network.mapper.toBook
import st.dominic.qrcodescanner.core.network.model.BookDocument
import st.dominic.qrcodescanner.core.network.model.BookDocument.Companion.BOOK_STATUS
import st.dominic.qrcodescanner.core.network.model.BookDocument.Companion.DATE_BORROWED
import st.dominic.qrcodescanner.core.network.model.BookDocument.Companion.STUDENT_ID
import javax.inject.Inject

class DefaultBookDataSource @Inject constructor(
    private val firestore: FirebaseFirestore,
) : BookDataSource {
    override fun getBorrowedBooksDocuments(studentId: String): Flow<List<Book>> {
        return firestore.collection(BOOK_COLLECTION).whereEqualTo(STUDENT_ID, studentId)
            .orderBy(DATE_BORROWED, Query.Direction.DESCENDING).snapshots()
            .mapNotNull { querySnapshots ->
                querySnapshots.mapNotNull { queryDocumentSnapshot ->
                    try {
                        toBook(bookDocument = queryDocumentSnapshot.toObject<BookDocument>())
                    } catch (e: RuntimeException) {
                        null
                    }
                }
            }.distinctUntilChanged()
    }

    override suspend fun addBook(book: Book) {
        val id = firestore.collection(BOOK_COLLECTION).add(book).await().id

        firestore.collection(BOOK_COLLECTION).document(id).update("id", id).await()
    }

    override suspend fun updateBookStatus(book: Book) {
        firestore.collection(BOOK_COLLECTION).document(book.id).update(
            mapOf(
                BOOK_STATUS to book.bookStatus
            )
        ).await()
    }

    override suspend fun deleteBook(id: String) {
        firestore.collection(BOOK_COLLECTION).document(id).delete().await()
    }
}