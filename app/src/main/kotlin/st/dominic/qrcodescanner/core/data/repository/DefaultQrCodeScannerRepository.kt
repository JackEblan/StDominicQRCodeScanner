package st.dominic.qrcodescanner.core.data.repository

import kotlinx.coroutines.flow.SharedFlow
import st.dominic.qrcodescanner.core.scanner.QrCodeScanner
import javax.inject.Inject

class DefaultQrCodeScannerRepository @Inject constructor(private val qrCodeScanner: QrCodeScanner) :
    QrCodeScannerRepository {
    override val moduleInstallProgress: SharedFlow<Float>
        get() = qrCodeScanner.moduleInstallProgress

    override suspend fun startScan(): Result<String> {
        return qrCodeScanner.startScan()
    }

    override suspend fun isScannerModuleAlreadyInstalled(): Result<Boolean> {
        return qrCodeScanner.isScannerModuleAlreadyInstalled()
    }

    override suspend fun isScannerModuleAvailable(): Result<Boolean> {
        return qrCodeScanner.isScannerModuleAvailable()
    }
}