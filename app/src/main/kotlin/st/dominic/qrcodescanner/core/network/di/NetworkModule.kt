package st.dominic.qrcodescanner.core.network.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import st.dominic.qrcodescanner.core.network.auth.DefaultEmailPasswordAuthentication
import st.dominic.qrcodescanner.core.network.auth.EmailPasswordAuthentication
import st.dominic.qrcodescanner.core.network.firestore.BookDataSource
import st.dominic.qrcodescanner.core.network.firestore.DefaultBookDataSource
import st.dominic.qrcodescanner.core.network.storage.DefaultStorageDataSource
import st.dominic.qrcodescanner.core.network.storage.StorageDataSource
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface NetworkModule {
    @Binds
    @Singleton
    fun bookDataSource(impl: DefaultBookDataSource): BookDataSource

    @Binds
    @Singleton
    fun storageDataSource(impl: DefaultStorageDataSource): StorageDataSource

    @Binds
    @Singleton
    fun emailPasswordAuthentication(impl: DefaultEmailPasswordAuthentication): EmailPasswordAuthentication
}