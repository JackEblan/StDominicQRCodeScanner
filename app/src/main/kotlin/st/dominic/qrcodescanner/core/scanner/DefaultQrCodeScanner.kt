package st.dominic.qrcodescanner.core.scanner

import com.google.android.gms.common.moduleinstall.ModuleInstallClient
import com.google.android.gms.common.moduleinstall.ModuleInstallRequest
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner
import kotlinx.coroutines.suspendCancellableCoroutine
import st.dominic.qrcodescanner.core.scanner.module.ModuleInstallProgressListener
import javax.inject.Inject
import kotlin.coroutines.resume

class DefaultQrCodeScanner @Inject constructor(
    private val scanner: GmsBarcodeScanner,
    private val moduleInstallClient: ModuleInstallClient,
    private val listener: ModuleInstallProgressListener
) : QrCodeScanner {

    override val moduleInstallProgress get() = listener.moduleInstallProgress

    override suspend fun startScan(): Result<String> {
        return suspendCancellableCoroutine { cancellableContinuation ->
            scanner.startScan().addOnSuccessListener { qrCode ->
                qrCode.rawValue?.let {
                    cancellableContinuation.resume(
                        Result.success(it)
                    )
                }
            }.addOnCanceledListener {
                cancellableContinuation.resume(
                    Result.failure(NullPointerException())
                )
            }.addOnFailureListener {
                cancellableContinuation.resume(
                    Result.failure(it)
                )
            }
        }
    }

    override suspend fun isScannerModuleAlreadyInstalled(): Result<Boolean> {
        val moduleInstallRequest =
            ModuleInstallRequest.newBuilder().addApi(scanner).setListener(listener).build()

        return suspendCancellableCoroutine { cancellableContinuation ->
            moduleInstallClient.installModules(moduleInstallRequest).addOnSuccessListener {
                cancellableContinuation.resume(
                    Result.success(it.areModulesAlreadyInstalled())
                )
            }.addOnFailureListener {
                cancellableContinuation.resume(
                    Result.failure(it)
                )
            }
        }
    }

    override suspend fun isScannerModuleAvailable(): Result<Boolean> {
        return suspendCancellableCoroutine { cancellableContinuation ->
            moduleInstallClient.areModulesAvailable(scanner).addOnSuccessListener {
                cancellableContinuation.resume(
                    Result.success(it.areModulesAvailable())
                )
            }.addOnFailureListener {
                cancellableContinuation.resume(
                    Result.failure(it)
                )
            }
        }
    }
}