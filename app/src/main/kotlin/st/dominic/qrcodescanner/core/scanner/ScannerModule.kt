package st.dominic.qrcodescanner.core.scanner

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface ScannerModule {
    @Binds
    @Singleton
    fun qrCodeScanner(impl: DefaultQrCodeScanner): QrCodeScanner
}