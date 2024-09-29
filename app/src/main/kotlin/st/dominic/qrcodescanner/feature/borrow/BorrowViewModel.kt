package st.dominic.qrcodescanner.feature.borrow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import st.dominic.qrcodescanner.core.data.repository.QrCodeScannerRepository
import st.dominic.qrcodescanner.core.domain.BorrowBookUseCase
import st.dominic.qrcodescanner.core.model.BorrowBookResult
import st.dominic.qrcodescanner.core.model.LocalBook
import st.dominic.qrcodescanner.core.model.QrCodeResult
import javax.inject.Inject

@HiltViewModel
class BorrowViewModel @Inject constructor(
    private val borrowBookUseCase: BorrowBookUseCase,
    private val qrCodeScannerRepository: QrCodeScannerRepository
) : ViewModel() {

    private val _borrowBookResultState = MutableStateFlow<BorrowBookResult?>(null)

    val borrowBookResultState = _borrowBookResultState.asStateFlow()

    private val _qrCodeResultState = MutableStateFlow<QrCodeResult?>(null)

    val qrCodeResultState = _qrCodeResultState.asStateFlow()

    fun borrowBook(localBook: LocalBook) {
        viewModelScope.launch {
            borrowBookUseCase(localBook = localBook).collect { uploadFileResult ->
                _borrowBookResultState.update {
                    uploadFileResult
                }
            }
        }
    }

    fun startScan() {
        viewModelScope.launch {
            _qrCodeResultState.update {
                qrCodeScannerRepository.startScan()
            }
        }
    }
}