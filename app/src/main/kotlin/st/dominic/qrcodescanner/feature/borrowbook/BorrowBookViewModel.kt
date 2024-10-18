package st.dominic.qrcodescanner.feature.borrowbook

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import st.dominic.qrcodescanner.core.data.repository.BookRepository
import st.dominic.qrcodescanner.core.data.repository.QrCodeScannerRepository
import st.dominic.qrcodescanner.core.domain.BorrowBookUseCase
import st.dominic.qrcodescanner.core.model.LocalBook
import javax.inject.Inject

@HiltViewModel
class BorrowBookViewModel @Inject constructor(
    private val borrowBookUseCase: BorrowBookUseCase,
    private val qrCodeScannerRepository: QrCodeScannerRepository,
    bookRepository: BookRepository,
) : ViewModel() {

    private val _borrowBookResultState = MutableStateFlow<Boolean?>(null)

    val borrowBookSuccess = _borrowBookResultState.asStateFlow()

    private val _qrCodeResultState = MutableStateFlow<String?>(null)

    val qrCodeResultState = _qrCodeResultState.asStateFlow()

    private val _qrCodeErrorState = MutableStateFlow<String?>(null)

    val qrCodeErrorState = _qrCodeErrorState.asStateFlow()

    val moduleInstallProgress = qrCodeScannerRepository.moduleInstallProgress.stateIn(
        scope = viewModelScope, started = SharingStarted.WhileSubscribed(5_000), initialValue = null
    )

    val bookProgress = bookRepository.progress.stateIn(
        scope = viewModelScope, started = SharingStarted.WhileSubscribed(5_000), initialValue = null
    )

    fun borrowBook(localBook: LocalBook) {
        viewModelScope.launch {
            borrowBookUseCase(localBook = localBook)

            _borrowBookResultState.update {
                true
            }
        }
    }

    fun isScannerModuleAvailable() {
        viewModelScope.launch {
            qrCodeScannerRepository.isScannerModuleAvailable().onSuccess { available ->
                if (available) {
                    startScan()
                } else {
                    isScannerModuleAlreadyInstalled()
                }
            }.onFailure {
                _qrCodeErrorState.update {
                    it
                }
            }
        }
    }

    private suspend fun isScannerModuleAlreadyInstalled() {
        qrCodeScannerRepository.isScannerModuleAlreadyInstalled().onSuccess { installed ->
            if (installed) {
                startScan()
            }
        }.onFailure {
            _qrCodeErrorState.update {
                it
            }
        }
    }

    private suspend fun startScan() {
        qrCodeScannerRepository.startScan().onSuccess { qrCode ->
            _qrCodeResultState.update {
                qrCode
            }
        }.onFailure {
            _qrCodeErrorState.update {
                it
            }
        }
    }
}