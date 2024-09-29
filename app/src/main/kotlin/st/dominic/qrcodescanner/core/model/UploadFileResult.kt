package st.dominic.qrcodescanner.core.model

data class UploadFileResult(
    val progress: Float? = null,
    val downloadLink: String? = null,
    val exception: Exception? = null,
)
