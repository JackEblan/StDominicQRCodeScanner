package st.dominic.qrcodescanner.core.scanner

import kotlinx.coroutines.flow.SharedFlow

interface QrCodeScanner {
    val moduleInstallProgress: SharedFlow<Float>

    suspend fun startScan(): Result<String>

    suspend fun isScannerModuleAlreadyInstalled(): Result<Boolean>

    suspend fun isScannerModuleAvailable(): Result<Boolean>
}