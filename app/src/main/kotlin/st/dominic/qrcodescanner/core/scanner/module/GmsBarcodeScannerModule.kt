package st.dominic.qrcodescanner.core.scanner.module

import android.content.Context
import com.google.android.gms.common.moduleinstall.ModuleInstall
import com.google.android.gms.common.moduleinstall.ModuleInstallClient
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GmsBarcodeScannerModule {

    @Provides
    @Singleton
    fun gmsBarcodeScannerOptions() = GmsBarcodeScannerOptions.Builder().setBarcodeFormats(
        Barcode.FORMAT_QR_CODE
    ).enableAutoZoom().build()

    @Provides
    @Singleton
    fun gmsBarcodeScanner(@ApplicationContext context: Context, options: GmsBarcodeScannerOptions) =
        GmsBarcodeScanning.getClient(context, options)

    @Provides
    @Singleton
    fun moduleInstallClient(@ApplicationContext context: Context) = ModuleInstall.getClient(context)

    @Provides
    @Singleton
    fun moduleInstallProgressListener(moduleInstallClient: ModuleInstallClient) =
        ModuleInstallProgressListener(moduleInstallClient = moduleInstallClient)

}