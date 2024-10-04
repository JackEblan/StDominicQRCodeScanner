package st.dominic.qrcodescanner.core.scanner

import com.google.android.gms.common.moduleinstall.InstallStatusListener
import com.google.android.gms.common.moduleinstall.ModuleInstallClient
import com.google.android.gms.common.moduleinstall.ModuleInstallStatusUpdate
import com.google.android.gms.common.moduleinstall.ModuleInstallStatusUpdate.InstallState.STATE_CANCELED
import com.google.android.gms.common.moduleinstall.ModuleInstallStatusUpdate.InstallState.STATE_COMPLETED
import com.google.android.gms.common.moduleinstall.ModuleInstallStatusUpdate.InstallState.STATE_FAILED
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

class ModuleInstallProgressListener @Inject constructor(private val moduleInstallClient: ModuleInstallClient) :
    InstallStatusListener {
    private val _moduleInstallProgress =
        MutableSharedFlow<Float>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    val moduleInstallProgress = _moduleInstallProgress.asSharedFlow()

    override fun onInstallStatusUpdated(update: ModuleInstallStatusUpdate) {
        update.progressInfo?.let {
            val progress = (it.bytesDownloaded * 100 / it.totalBytesToDownload)

            _moduleInstallProgress.tryEmit(progress.toFloat())
        }

        if (isTerminateState(update.installState)) {
            moduleInstallClient.unregisterListener(this)
        }
    }

    private fun isTerminateState(@ModuleInstallStatusUpdate.InstallState state: Int): Boolean {
        return state == STATE_CANCELED || state == STATE_COMPLETED || state == STATE_FAILED
    }
}
