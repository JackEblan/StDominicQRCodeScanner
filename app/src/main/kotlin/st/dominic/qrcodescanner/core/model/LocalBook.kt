package st.dominic.qrcodescanner.core.model

import android.net.Uri

data class LocalBook(
    val imageUri: Uri,
    val qrCode: String,
    val title: String,
    val author: String,
)