package st.dominic.qrcodescanner.core.scanner

import st.dominic.qrcodescanner.core.model.QrCodeResult

interface QrCodeScanner {
    suspend fun startScan(): QrCodeResult
}