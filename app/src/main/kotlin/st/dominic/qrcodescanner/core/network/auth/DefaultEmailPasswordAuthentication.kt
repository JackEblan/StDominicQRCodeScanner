package st.dominic.qrcodescanner.core.network.auth

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import st.dominic.qrcodescanner.core.common.Dispatcher
import st.dominic.qrcodescanner.core.common.StDominicQrCodeScannerDispatchers.IO
import st.dominic.qrcodescanner.core.model.AuthCurrentUser
import st.dominic.qrcodescanner.core.network.mapper.toAuthCurrentUser
import javax.inject.Inject
import kotlin.coroutines.resume

class DefaultEmailPasswordAuthentication @Inject constructor(
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher,
    private val firebaseAuth: FirebaseAuth,
) : EmailPasswordAuthentication {

    override suspend fun createUserWithEmailAndPassword(
        name: String, email: String, password: String
    ): Result<Boolean> {
        return withContext(ioDispatcher) {
            suspendCancellableCoroutine { continuation ->
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        updateProfile(
                            createUserWithEmailAndPasswordTask = task,
                            name = name,
                            continuation = continuation
                        )
                    }
            }
        }
    }

    override suspend fun signInWithEmailAndPassword(
        email: String, password: String
    ): Result<Boolean> {
        return withContext(ioDispatcher) {
            suspendCancellableCoroutine { continuation ->
                firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            continuation.resume(Result.success(task.isSuccessful))
                        } else {
                            task.exception?.let {
                                continuation.resume(Result.failure(it))
                            }
                        }
                    }
            }
        }
    }

    override fun getCurrentUser(): AuthCurrentUser? {
        return toAuthCurrentUser(firebaseAuth.currentUser)
    }

    override suspend fun sendEmailVerification(): Result<Boolean> {
        return withContext(ioDispatcher) {
            suspendCancellableCoroutine { continuation ->
                firebaseAuth.currentUser?.sendEmailVerification()?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resume(Result.success(task.isSuccessful))
                    } else task.exception?.let {
                        continuation.resume(Result.failure(it))
                    }
                }
            }
        }
    }

    override suspend fun sendPasswordResetEmail(email: String): Result<Boolean> {
        return withContext(ioDispatcher) {
            suspendCancellableCoroutine { continuation ->
                firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resume(Result.success(task.isSuccessful))
                    } else task.exception?.let {
                        continuation.resume(Result.failure(it))
                    }
                }
            }
        }
    }

    override fun signOut() {
        firebaseAuth.signOut()
    }

    private fun updateProfile(
        createUserWithEmailAndPasswordTask: Task<AuthResult>,
        name: String,
        continuation: CancellableContinuation<Result<Boolean>>
    ) {
        if (createUserWithEmailAndPasswordTask.isSuccessful) {
            val userProfileChangeRequest =
                UserProfileChangeRequest.Builder().setDisplayName(name).build()

            try {
                firebaseAuth.currentUser?.updateProfile(userProfileChangeRequest)
                    ?.addOnCompleteListener { updateProfileTask ->
                        continuation.resume(Result.success(updateProfileTask.isSuccessful))
                    }
            } catch (e: FirebaseAuthInvalidUserException) {
                continuation.resume(Result.failure(e))
            }
        } else {
            createUserWithEmailAndPasswordTask.exception?.let {
                continuation.resume(Result.failure(it))
            }
        }
    }
}