package st.dominic.qrcodescanner.core.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import st.dominic.qrcodescanner.core.data.repository.BookRepository
import st.dominic.qrcodescanner.core.data.repository.EmailPasswordAuthenticationRepository
import st.dominic.qrcodescanner.core.model.Book
import st.dominic.qrcodescanner.core.model.BookStatus
import st.dominic.qrcodescanner.core.model.BorrowBookResult
import st.dominic.qrcodescanner.core.model.LocalBook
import javax.inject.Inject

class BorrowBookUseCase @Inject constructor(
    private val bookRepository: BookRepository,
    private val emailPasswordAuthenticationRepository: EmailPasswordAuthenticationRepository
) {
    suspend operator fun invoke(localBook: LocalBook): Flow<BorrowBookResult> {
        val bookId = bookRepository.generateBookId()

        val currentUser = emailPasswordAuthenticationRepository.getCurrentUser() ?: return flowOf(
            BorrowBookResult(notSignedIn = true)
        )

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
                        studentId = currentUser.uid,
                        studentName = currentUser.displayName,
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