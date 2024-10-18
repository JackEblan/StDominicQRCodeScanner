package st.dominic.qrcodescanner.core.domain

import st.dominic.qrcodescanner.core.data.repository.BookRepository
import st.dominic.qrcodescanner.core.data.repository.EmailPasswordAuthenticationRepository
import st.dominic.qrcodescanner.core.model.GetBooksResult
import javax.inject.Inject

class GetBooksUseCase @Inject constructor(
    private val bookRepository: BookRepository,
    private val emailPasswordAuthenticationRepository: EmailPasswordAuthenticationRepository,
) {
    suspend operator fun invoke(): GetBooksResult {
        val currentUser = emailPasswordAuthenticationRepository.getCurrentUser()
            ?: return GetBooksResult.NotSignedIn

        return if (currentUser.isEmailVerified) {
            GetBooksResult.Success(bookRepository.getBorrowedBooksByStudentId(studentId = currentUser.uid))
        } else {
            return GetBooksResult.NotEmailVerified
        }
    }
}