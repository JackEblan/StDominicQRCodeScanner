package st.dominic.qrcodescanner.core.domain

import st.dominic.qrcodescanner.core.data.repository.BookRepository
import st.dominic.qrcodescanner.core.data.repository.EmailPasswordAuthenticationRepository
import st.dominic.qrcodescanner.core.model.Book
import javax.inject.Inject

class GetBooksUseCase @Inject constructor(
    private val bookRepository: BookRepository,
    private val emailPasswordAuthenticationRepository: EmailPasswordAuthenticationRepository,
) {
    suspend operator fun invoke(): List<Book> {
        val studentId = emailPasswordAuthenticationRepository.getCurrentUser()?.uid

        return if (studentId != null) {
            bookRepository.getBorrowedBooksByStudentId(studentId = studentId)
        } else {
            emptyList()
        }
    }
}