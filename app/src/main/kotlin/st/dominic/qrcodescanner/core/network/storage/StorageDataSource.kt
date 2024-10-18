package st.dominic.qrcodescanner.core.network.storage

import android.net.Uri
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import st.dominic.qrcodescanner.core.model.UploadFileResult

interface StorageDataSource {
    val progress: SharedFlow<Float?>

    suspend fun uploadFile(
        file: Uri, ref: String, fileName: String
    ): Result<String>
}