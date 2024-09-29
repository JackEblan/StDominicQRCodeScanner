package st.dominic.qrcodescanner.core.data.repository

import st.dominic.qrcodescanner.core.model.QrCodeResult
import st.dominic.qrcodescanner.core.scanner.QrCodeScanner
import javax.inject.Inject

class DefaultQrCodeScannerRepository @Inject constructor(private val qrCodeScanner: QrCodeScanner) :
    QrCodeScannerRepository {
    override suspend fun startScan(): QrCodeResult {
        return qrCodeScanner.startScan()
    }
}