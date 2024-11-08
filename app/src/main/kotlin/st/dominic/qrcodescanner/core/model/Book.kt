package st.dominic.qrcodescanner.core.model

data class Book(
    val id: String,
    val imageUrl: String,
    val qrCode: String,
    val title: String,
    val author: String,
    val dateBorrowed: String?,
    val dateReturned: String?,
    val studentId: String,
    val studentName: String,
    val bookStatus: BookStatus,
)

enum class BookStatus {
    Borrowed, Returned,
}