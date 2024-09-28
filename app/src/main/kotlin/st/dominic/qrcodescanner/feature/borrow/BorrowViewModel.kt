package st.dominic.qrcodescanner.feature.borrow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import st.dominic.qrcodescanner.core.data.repository.BookRepository
import st.dominic.qrcodescanner.core.model.Book
import javax.inject.Inject

@HiltViewModel
class BorrowViewModel @Inject constructor(private val bookRepository: BookRepository) :
    ViewModel() {

    fun addBook(book: Book) {
        viewModelScope.launch {
            bookRepository.addBook(book = book)
        }
    }
}