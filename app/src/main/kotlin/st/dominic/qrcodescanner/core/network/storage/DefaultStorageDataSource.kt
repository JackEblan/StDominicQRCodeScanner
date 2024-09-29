package st.dominic.qrcodescanner.core.network.storage

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import st.dominic.qrcodescanner.core.common.Dispatcher
import st.dominic.qrcodescanner.core.common.StDominicQrCodeScannerDispatchers
import st.dominic.qrcodescanner.core.model.UploadFileResult
import javax.inject.Inject

class DefaultStorageDataSource @Inject constructor(
    private val storage: FirebaseStorage,
    @Dispatcher(StDominicQrCodeScannerDispatchers.IO) private val ioDispatcher: CoroutineDispatcher
) : StorageDataSource {

    override fun uploadFile(
        file: Uri, ref: String, fileName: String
    ): Flow<UploadFileResult> {
        return callbackFlow {
            val photosRef = storage.reference.child(ref).child(fileName)
            val uploadTask = photosRef.putFile(file)

            uploadTask.addOnProgressListener { snapshot ->
                val progress = (100.0 * snapshot.bytesTransferred) / snapshot.totalByteCount

                trySend(UploadFileResult(progress = progress.toFloat()))
            }.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                photosRef.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    trySend(UploadFileResult(downloadLink = downloadUri.toString()))
                } else {
                    task.exception?.let {
                        trySend(
                            UploadFileResult(exception = it)
                        )
                    }
                }
            }

            awaitClose {
                uploadTask.cancel()
            }

        }.conflate().flowOn(ioDispatcher)
    }
}