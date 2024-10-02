package st.dominic.qrcodescanner.core.network.firestore

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.snapshots
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.tasks.await
import st.dominic.qrcodescanner.core.model.Book
import st.dominic.qrcodescanner.core.model.BookStatus
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
    override fun getBorrowedBooksDocumentsByStudentId(studentId: String): Flow<List<Book>> {
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

    override fun getBorrowedBooksDocuments(): Flow<List<Book>> {
        return firestore.collection(BOOK_COLLECTION)
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
        firestore.collection(BOOK_COLLECTION).document(book.id).set(book).await()

        firestore.collection(BOOK_COLLECTION).document(book.id)
            .update("dateBorrowed", FieldValue.serverTimestamp())
    }

    override suspend fun updateBookStatus(id: String, bookStatus: BookStatus) {
        firestore.collection(BOOK_COLLECTION).document(id).update(
            mapOf(
                "dateReturned" to FieldValue.serverTimestamp(),
                BOOK_STATUS to bookStatus,
            )
        ).await()
    }

    override suspend fun deleteBook(id: String) {
        firestore.collection(BOOK_COLLECTION).document(id).delete().await()
    }

    override fun generateBookId(): String {
        return firestore.collection(BOOK_COLLECTION).document().id
    }

    override suspend fun getBook(id: String): Book? {
        val documentSnapshot = firestore.collection(BOOK_COLLECTION).document(id).get().await()

        return try {
            toBook(bookDocument = documentSnapshot.toObject<BookDocument>())
        } catch (e: RuntimeException) {
            null
        }
    }
}