package st.dominic.qrcodescanner.core.data.repository

import kotlinx.coroutines.flow.SharedFlow

interface QrCodeScannerRepository {
    val moduleInstallProgress: SharedFlow<Float>

    suspend fun startScan(): Result<String>

    suspend fun isScannerModuleAlreadyInstalled(): Result<Boolean>

    suspend fun isScannerModuleAvailable(): Result<Boolean>
}