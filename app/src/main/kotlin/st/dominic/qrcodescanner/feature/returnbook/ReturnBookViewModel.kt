package st.dominic.qrcodescanner.feature.returnbook

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import st.dominic.qrcodescanner.core.data.repository.BookRepository
import st.dominic.qrcodescanner.core.data.repository.QrCodeScannerRepository
import st.dominic.qrcodescanner.core.model.BookStatus
import st.dominic.qrcodescanner.feature.returnbook.navigation.ReturnBookRouteData
import javax.inject.Inject

@HiltViewModel
class ReturnBookViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val bookRepository: BookRepository,
    private val qrCodeScannerRepository: QrCodeScannerRepository,
) : ViewModel() {
    private val returnBookRouteData = savedStateHandle.toRoute<ReturnBookRouteData>()

    private val _returnBookUiState = MutableStateFlow<ReturnBookUiState?>(null)

    private val _qrCodeResultState = MutableStateFlow<String?>(null)

    val qrCodeResult = _qrCodeResultState.asStateFlow()

    private val _qrCodeErrorState = MutableStateFlow<String?>(null)

    val qrCodeError = _qrCodeErrorState.asStateFlow()

    val adminUiState = _returnBookUiState.onStart { getBook() }.stateIn(
        scope = viewModelScope, started = SharingStarted.WhileSubscribed(5_000), initialValue = null
    )

    val moduleInstallProgress = qrCodeScannerRepository.moduleInstallProgress.stateIn(
        scope = viewModelScope, started = SharingStarted.WhileSubscribed(5_000), initialValue = null
    )

    fun returnBook() {
        viewModelScope.launch {
            _returnBookUiState.update {
                ReturnBookUiState.Loading
            }

            bookRepository.updateBookStatus(
                id = returnBookRouteData.id, bookStatus = BookStatus.Returned
            )

            _returnBookUiState.update {
                ReturnBookUiState.Returned
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

    private fun getBook() {
        viewModelScope.launch {
            _returnBookUiState.update {
                ReturnBookUiState.Success(book = bookRepository.getBook(id = returnBookRouteData.id))
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