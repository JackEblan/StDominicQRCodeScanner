package st.dominic.qrcodescanner.core.network.mapper

import st.dominic.qrcodescanner.core.model.Book
import st.dominic.qrcodescanner.core.model.BookStatus
import st.dominic.qrcodescanner.core.network.model.BookDocument
import java.text.DateFormat

fun toBook(bookDocument: BookDocument): Book {
    val id = bookDocument.id.toString()

    val imageUrl = bookDocument.imageUrl.toString()

    val qrCode = bookDocument.qrCode.toString()

    val title = bookDocument.title.toString()

    val author = bookDocument.author.toString()

    val dateBorrowed =
        bookDocument.dateBorrowed?.toDate()?.let(DateFormat.getDateInstance()::format).toString()

    val dateReturned =
        bookDocument.dateReturned?.toDate()?.let(DateFormat.getDateInstance()::format)

    val studentId = bookDocument.studentId.toString()

    val studentName = bookDocument.studentName.toString()

    val bookStatus = bookDocument.bookStatus ?: BookStatus.Borrowed

    return Book(
        id = id,
        imageUrl = imageUrl,
        qrCode = qrCode,
        title = title,
        author = author,
        dateBorrowed = dateBorrowed,
        dateReturned = dateReturned,
        studentId = studentId,
        studentName = studentName,
        bookStatus = bookStatus
    )
}