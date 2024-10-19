package st.dominic.qrcodescanner.core.domain

import st.dominic.qrcodescanner.core.data.repository.BookRepository
import st.dominic.qrcodescanner.core.data.repository.EmailPasswordAuthenticationRepository
import st.dominic.qrcodescanner.core.model.Book
import st.dominic.qrcodescanner.core.model.BookStatus
import st.dominic.qrcodescanner.core.model.LocalBook
import javax.inject.Inject

class BorrowBookUseCase @Inject constructor(
    private val bookRepository: BookRepository,
    private val emailPasswordAuthenticationRepository: EmailPasswordAuthenticationRepository
) {
    suspend operator fun invoke(localBook: LocalBook) {
        val bookId = bookRepository.generateBookId()

        val currentUser = emailPasswordAuthenticationRepository.getCurrentUser() ?: return

        bookRepository.uploadBookPhoto(
            file = localBook.imageUri, fileName = bookId
        ).onSuccess { imageUrl->
            bookRepository.addBook(
                Book(
                    id = bookId,
                    imageUrl = imageUrl,
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
        }
    }
}