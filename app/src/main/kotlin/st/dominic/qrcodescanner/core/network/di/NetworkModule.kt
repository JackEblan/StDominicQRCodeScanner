package st.dominic.qrcodescanner.core.network.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import st.dominic.qrcodescanner.core.network.firestore.BookDataSource
import st.dominic.qrcodescanner.core.network.firestore.DefaultBookDataSource
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface NetworkModule {
    @Binds
    @Singleton
    fun bookDataSource(impl: DefaultBookDataSource): BookDataSource
}