package st.dominic.qrcodescanner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dagger.hilt.android.AndroidEntryPoint
import st.dominic.qrcodescanner.core.designsystem.theme.StDominicQRCodeScannerTheme
import st.dominic.qrcodescanner.navigation.StDominicQrCodeScannerNavHost

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            StDominicQRCodeScannerTheme {
                StDominicQrCodeScannerNavHost()
            }
        }
    }
}