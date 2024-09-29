package st.dominic.qrcodescanner.core.scanner

import android.content.Context
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.suspendCancellableCoroutine
import st.dominic.qrcodescanner.core.model.QrCodeResult
import javax.inject.Inject
import kotlin.coroutines.resume

class DefaultQrCodeScanner @Inject constructor(@ApplicationContext private val context: Context) :
    QrCodeScanner {
    private val options = GmsBarcodeScannerOptions.Builder().setBarcodeFormats(
        Barcode.FORMAT_QR_CODE
    ).enableAutoZoom().build()

    private val scanner = GmsBarcodeScanning.getClient(context, options)

    override suspend fun startScan(): QrCodeResult {
        return suspendCancellableCoroutine { cancellableContinuation ->
            scanner.startScan().addOnSuccessListener { barcode ->
                cancellableContinuation.resume(
                    QrCodeResult(
                        rawValue = barcode.rawValue, exception = null
                    )
                )
            }.addOnCanceledListener {
                cancellableContinuation.resume(
                    QrCodeResult(
                        rawValue = null, exception = null
                    )
                )
            }.addOnFailureListener { e ->
                cancellableContinuation.resume(
                    QrCodeResult(
                        rawValue = null, exception = e
                    )
                )
            }
        }
    }
}