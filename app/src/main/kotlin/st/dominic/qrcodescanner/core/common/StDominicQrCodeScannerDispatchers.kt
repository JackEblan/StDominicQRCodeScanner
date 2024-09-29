package st.dominic.qrcodescanner.core.common

import javax.inject.Qualifier
import kotlin.annotation.AnnotationRetention.RUNTIME

@Qualifier
@Retention(RUNTIME)
annotation class Dispatcher(val stDominicQrCodeScannerDispatchers: StDominicQrCodeScannerDispatchers)

enum class StDominicQrCodeScannerDispatchers {
    Default,
    IO,
}
