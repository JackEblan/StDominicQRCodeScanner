package st.dominic.qrcodescanner.core.network.storage

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

class DefaultStorageDataSource @Inject constructor(
    private val storage: FirebaseStorage
) : StorageDataSource {
    private val _progress =
        MutableSharedFlow<Float?>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    override val progress = _progress.asSharedFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun uploadFile(
        file: Uri, ref: String, fileName: String
    ): Result<String> {
        return runCatching {
            val photosRef = storage.reference.child(ref).child(fileName)

            val uploadTask = photosRef.putFile(file)

            suspendCancellableCoroutine { continuation ->
                uploadTask.addOnProgressListener { snapshot ->
                    _progress.tryEmit(snapshot.bytesTransferred * 100F / snapshot.totalByteCount)

                }.continueWithTask { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let {
                            throw it
                        }
                    }

                    photosRef.downloadUrl
                }.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val downloadUri = task.result.toString()

                        continuation.resume(downloadUri)
                    } else {
                        task.exception?.let {
                            throw it
                        }
                    }
                }

                continuation.invokeOnCancellation {
                    _progress.resetReplayCache()

                    uploadTask.cancel()
                }
            }
        }
    }
}