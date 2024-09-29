package st.dominic.qrcodescanner.core.network.storage

import android.net.Uri
import kotlinx.coroutines.flow.Flow
import st.dominic.qrcodescanner.core.model.UploadFileResult

interface StorageDataSource {
    fun uploadFile(
        file: Uri, ref: String, fileName: String
    ): Flow<UploadFileResult>
}