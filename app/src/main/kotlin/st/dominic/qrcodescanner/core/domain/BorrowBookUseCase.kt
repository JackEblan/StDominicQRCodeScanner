package st.dominic.qrcodescanner.core.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import st.dominic.qrcodescanner.core.data.repository.BookRepository
import st.dominic.qrcodescanner.core.model.Book
import st.dominic.qrcodescanner.core.model.BookStatus
import st.dominic.qrcodescanner.core.model.BorrowBookResult
import st.dominic.qrcodescanner.core.model.LocalBook
import javax.inject.Inject

class BorrowBookUseCase @Inject constructor(private val bookRepository: BookRepository) {
    suspend operator fun invoke(localBook: LocalBook): Flow<BorrowBookResult> {
        val bookId = bookRepository.generateBookId()

        return bookRepository.uploadBookPhoto(
            file = localBook.imageUri, fileName = bookId
        ).map { uploadFileResult ->
            if (uploadFileResult.downloadLink != null) {
                bookRepository.addBook(
                    Book(
                        id = bookId,
                        imageUrl = uploadFileResult.downloadLink,
                        qrCode = localBook.qrCode,
                        title = localBook.title,
                        author = localBook.author,
                        dateBorrowed = null,
                        dateReturned = null,
                        studentId = "id",
                        studentName = "studentName",
                        bookStatus = BookStatus.Borrowed
                    )
                )

                BorrowBookResult(
                    progress = uploadFileResult.progress,
                    downloadLink = uploadFileResult.downloadLink,
                    exception = uploadFileResult.exception,
                    success = true
                )
            } else {
                BorrowBookResult(
                    progress = uploadFileResult.progress,
                    exception = uploadFileResult.exception,
                    success = false
                )
            }
        }
    }
}