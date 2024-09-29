package st.dominic.qrcodescanner.core.data.repository

import st.dominic.qrcodescanner.core.model.QrCodeResult

interface QrCodeScannerRepository {
    suspend fun startScan(): QrCodeResult
}