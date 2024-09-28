package st.dominic.qrcodescanner.core.network.model

import com.google.firebase.Timestamp
import st.dominic.qrcodescanner.core.model.BookStatus

data class BookDocument(
    val id: String? = null,
    val imageUrl: String? = null,
    val title: String? = null,
    val author: String? = null,
    val dateBorrowed: Timestamp? = null,
    val dateReturned: Timestamp? = null,
    val studentId: String? = null,
    val studentName: String? = null,
    val bookStatus: BookStatus? = null,
){
    companion object {
        const val STUDENT_ID = "studentId"
        const val DATE_BORROWED = "dateBorrowed"
        const val BOOK_STATUS = "bookStatus"
    }
}
