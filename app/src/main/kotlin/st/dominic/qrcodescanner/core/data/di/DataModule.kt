package st.dominic.qrcodescanner.core.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import st.dominic.qrcodescanner.core.data.repository.AdminRepository
import st.dominic.qrcodescanner.core.data.repository.BookRepository
import st.dominic.qrcodescanner.core.data.repository.DefaultAdminRepository
import st.dominic.qrcodescanner.core.data.repository.DefaultBookRepository
import st.dominic.qrcodescanner.core.data.repository.DefaultEmailPasswordAuthenticationRepository
import st.dominic.qrcodescanner.core.data.repository.DefaultQrCodeScannerRepository
import st.dominic.qrcodescanner.core.data.repository.EmailPasswordAuthenticationRepository
import st.dominic.qrcodescanner.core.data.repository.QrCodeScannerRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    @Singleton
    fun bookRepository(impl: DefaultBookRepository): BookRepository

    @Binds
    @Singleton
    fun qrCodeScannerRepository(impl: DefaultQrCodeScannerRepository): QrCodeScannerRepository

    @Binds
    @Singleton
    fun emailPasswordAuthenticationRepository(impl: DefaultEmailPasswordAuthenticationRepository): EmailPasswordAuthenticationRepository

    @Binds
    @Singleton
    fun adminRepository(impl: DefaultAdminRepository): AdminRepository
}